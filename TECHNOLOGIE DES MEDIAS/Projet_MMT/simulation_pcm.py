import numpy as np
import soundfile as sf
from scipy import signal
import matplotlib.pyplot as plt
import os

# Create output directories if they don't exist
os.makedirs('PCM', exist_ok=True)

# Parametres signaux
f0 = 2000.0  # 2 kHz
T = 3.0      # 3 seconds
samples_per_period = 10  # 10 echantillons par periode
Fs = int(f0 * samples_per_period)  # frequence d'echantillonnage

print(f"Fs = {Fs} Hz")

t = np.arange(0, T, 1.0/Fs)
# Signal de reference (sinus)
x = 0.9 * np.sin(2*np.pi*f0*t)  # amplitude 0.9 pour eviter saturation

# Fonctions utilitaires PCM

def quantize(x, bits):
    if bits <= 0:
        # 1-bit equiv : signe
        return (x >= 0).astype(np.int8)
    levels = 2 ** bits
    # uniform mid-tread quantizer between -1 and 1
    # Ensure quantization levels include -1, 0, and 1
    step = 2.0 / (levels - 1)
    q = np.round((x + 1.0) / step).astype(np.int32)
    q = np.clip(q, 0, levels-1)
    # reconstruction: map back to [-1, 1] range with 0 at center
    xr = q * step - 1.0
    return q, xr


def pcm_encode(x, bits):
    if bits <= 0:
        # return bits stream of sign
        bits_stream = (x >= 0).astype(np.uint8)
        return bits_stream, (bits_stream*2-1).astype(np.float32)
    q, xr = quantize(x, bits)
    # convert q to bits
    bits_stream = ((q[:, None] & (1 << np.arange(bits))) > 0).astype(np.uint8)
    # LSB first -> reshape
    bits_stream = bits_stream.flatten()
    return bits_stream, xr


def pcm_decode(bits_stream, bits, nsamples):
    if bits <= 0:
        xr = (bits_stream[:nsamples]*2-1).astype(np.float32)
        return xr
    q = np.zeros(nsamples, dtype=np.int32)
    bs = bits_stream.reshape(nsamples, bits)
    for i in range(bits):
        q |= (bs[:, i].astype(np.int32) << i)
    levels = 2**bits
    step = 2.0 / (levels - 1)
    xr = q * step - 1.0
    return xr


def bits_errors(bits, p):
    # flip each bit with probability p
    err = np.random.rand(bits.size) < p
    out = bits.copy()
    out[err] = 1 - out[err]
    return out


def snr_db(original, reconstructed):
    e = original - reconstructed
    Psignal = np.mean(original**2)
    Pnoise = np.mean(e**2)
    return 10*np.log10(Psignal / Pnoise)


# Partie 1: PCM
print("\n=== PCM Encoding ===\n")
bits_list = [8, 6, 4, 3, 2, 1]
results = {}
pcm_reconstructed = {}

for b in bits_list:
    print(f"Quantification PCM {b} bits...")
    bits_stream, xr = pcm_encode(x, b)
    if b>0:
        # reconstruct from quantized xr
        rec = xr
    else:
        rec = xr
    s = snr_db(x, rec)
    results[f'PCM_{b}'] = s
    pcm_reconstructed[b] = rec
    print(f"SNR PCM {b} bits: {s:.2f} dB")
    # save WAV (scaled to int16)
    sf.write(f'PCM/pcm_{b}bits.wav', (rec*0.9).astype(np.float32), Fs)

# Plot PCM comparisons
print("\nGenerating PCM comparison plots...")
# Show a zoomed section for better visibility
t_start = 0.0
t_end = 0.003  # 3ms window to see details
idx_start = int(t_start * Fs)
idx_end = int(t_end * Fs)
t_zoom = t[idx_start:idx_end]

# Create figure with subplots for each quantization level
fig, axes = plt.subplots(3, 2, figsize=(15, 12))
fig.suptitle('PCM Quantization Comparison (3ms window)', fontsize=16)

for idx, b in enumerate(bits_list):
    row = idx // 2
    col = idx % 2
    ax = axes[row, col]
    
    ax.plot(t_zoom * 1000, x[idx_start:idx_end], 'b-', label='Original', linewidth=1.5, alpha=0.7)
    ax.step(t_zoom * 1000, pcm_reconstructed[b][idx_start:idx_end], 'r-', label=f'PCM {b} bits', linewidth=1.5, where='post')
    ax.set_xlabel('Time (ms)')
    ax.set_ylabel('Amplitude')
    ax.set_title(f'PCM {b} bits (SNR: {results[f"PCM_{b}"]:.2f} dB)')
    ax.legend()
    ax.grid(True, alpha=0.3)
    ax.set_ylim(-1, 1)
    ax.set_yticks([-1, 0, 1])

plt.tight_layout()
plt.savefig('PCM/pcm_comparison.png', dpi=300, bbox_inches='tight')
print("Saved: PCM/pcm_comparison.png")

# Create a single plot showing all PCM levels together
fig2, ax2 = plt.subplots(figsize=(14, 8))
ax2.plot(t_zoom * 1000, x[idx_start:idx_end], 'k-', label='Original', linewidth=2, alpha=0.8)

colors = ['red', 'orange', 'green', 'blue', 'purple', 'brown']
for idx, b in enumerate(bits_list):
    ax2.step(t_zoom * 1000, pcm_reconstructed[b][idx_start:idx_end], 
             '-', color=colors[idx], label=f'PCM {b} bits (SNR: {results[f"PCM_{b}"]:.1f} dB)', 
             linewidth=1.5, alpha=0.7, where='post')

ax2.set_xlabel('Time (ms)', fontsize=12)
ax2.set_ylabel('Amplitude', fontsize=12)
ax2.set_title('PCM Quantization Comparison - All Levels (3ms window)', fontsize=14)
ax2.legend(loc='best')
ax2.grid(True, alpha=0.3)
ax2.set_ylim(-1, 1)
ax2.set_yticks([-1, 0, 1])
plt.tight_layout()
plt.savefig('PCM/pcm_all_comparison.png', dpi=300, bbox_inches='tight')
print("Saved: PCM/pcm_all_comparison.png")
plt.close('all')

print("\nPCM simulation done.")
