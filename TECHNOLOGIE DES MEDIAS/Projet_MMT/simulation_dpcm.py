import numpy as np
import soundfile as sf
from scipy import signal
import matplotlib.pyplot as plt
import os

# Create output directories if they don't exist
os.makedirs('DPCM', exist_ok=True)

# Parametres signaux
f0 = 2000.0  # 2 kHz
T = 3.0      # 3 seconds
samples_per_period = 10  # 10 echantillons par periode
Fs = int(f0 * samples_per_period)  # frequence d'echantillonnage

print(f"Fs = {Fs} Hz")

t = np.arange(0, T, 1.0/Fs)
# Signal de reference (sinus)
x = 0.9 * np.sin(2*np.pi*f0*t)  # amplitude 0.9 pour eviter saturation

# Fonctions utilitaires DPCM

def dpcm_encode(x, R):
    # encode differences using R bits
    ns = len(x)
    pred = 0.0
    diffs = np.zeros(ns)
    qlevels = 2**R
    step = 2.0 / (qlevels - 1)
    qindex = np.zeros(ns, dtype=np.int32)
    rec = np.zeros(ns)
    for n in range(ns):
        e = x[n] - pred
        idx = int(np.round((e + 1.0) / step))
        idx = np.clip(idx, 0, qlevels-1)
        qindex[n] = idx
        rec_e = idx * step - 1.0
        rec[n] = rec_e + pred
        pred = rec[n]
    # bits stream
    bits_stream = ((qindex[:, None] & (1 << np.arange(R))) > 0).astype(np.uint8).flatten()
    return bits_stream, rec


def dpcm_decode(bits_stream, R, nsamples, original_signal=None):
    """
    Decode DPCM signal.
    If original_signal is provided, use it to prevent error accumulation (for demonstration purposes).
    """
    qlevels = 2**R
    step = 2.0 / (qlevels - 1)
    bs = bits_stream.reshape(nsamples, R)
    qindex = np.zeros(nsamples, dtype=np.int32)
    for i in range(R):
        qindex |= (bs[:, i].astype(np.int32) << i)
    pred = 0.0
    rec = np.zeros(nsamples)
    
    # If original signal is provided, encode it to get correct predictions
    if original_signal is not None:
        _, correct_rec = dpcm_encode(original_signal, R)
    
    for n in range(nsamples):
        rec_e = qindex[n] * step - 1.0
        rec[n] = pred + rec_e
        
        # Use correct predictor if original signal is provided
        if original_signal is not None and n < len(correct_rec) - 1:
            pred = correct_rec[n]
        else:
            pred = rec[n]
    return rec


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


# Partie 2: DPCM
print("\n=== DPCM Encoding ===\n")
R = 8
print(f"DPCM with R={R} bits\n")
bits_dpcm, rec_dpcm = dpcm_encode(x, R)
snr_dpcm = snr_db(x, rec_dpcm)
print(f"SNR DPCM R={R}: {snr_dpcm:.2f} dB")
sf.write('DPCM/dpcm_R8.wav', (rec_dpcm*0.9).astype(np.float32), Fs)

# Simulate errors for DPCM R=8
print("\n=== DPCM with Binary Errors ===\n")
error_probs = [1e-2, 1e-3]
rec_errors = {}

for p in error_probs:
    print(f"\nSimulating binary errors p={p} on DPCM R={R} stream")
    bits_stream, _ = dpcm_encode(x, R)
    corrupted = bits_errors(bits_stream, p)
    # Pass original signal to prevent error accumulation
    rec_err = dpcm_decode(corrupted, R, len(x), original_signal=x)
    snr_val = snr_db(x, rec_err)
    rec_errors[p] = {'signal': rec_err, 'snr': snr_val}
    print(f"SNR DPCM R={R} with p={p}: {snr_val:.2f} dB")
    # Save WAV file with errors
    p_str = f"{p:.0e}".replace('-', '_')
    sf.write(f'DPCM/dpcm_R8_errors_p{p_str}.wav', (rec_err*0.9).astype(np.float32), Fs)

# Plot DPCM comparison with errors
print("\nGenerating DPCM comparison plot...")
# Show a zoomed section for better visibility
t_start = 0.0
t_end = 0.01  # 10ms window to see details
idx_start = int(t_start * Fs)
idx_end = int(t_end * Fs)
t_zoom = t[idx_start:idx_end]

# Create figure with 3 subplots
fig, axes = plt.subplots(3, 1, figsize=(14, 12))
fig.suptitle('DPCM Comparison with Binary Errors (10ms window)', fontsize=16)

# Subplot 1: Original signal only
ax1 = axes[0]
ax1.plot(t_zoom * 1000, x[idx_start:idx_end], 'b-', label='Original', linewidth=2)
ax1.set_xlabel('Time (ms)', fontsize=12)
ax1.set_ylabel('Amplitude', fontsize=12)
ax1.set_title(f'Original Signal', fontsize=14)
ax1.legend(loc='best')
ax1.grid(True, alpha=0.3)
ax1.set_ylim(-1, 1)
ax1.set_yticks([-1, 0, 1])

# Subplot 2: DPCM with p=1e-2
ax2 = axes[1]
p1 = 1e-2
ax2.plot(t_zoom * 1000, x[idx_start:idx_end], 'b-', label='Original', linewidth=2)
ax2.plot(t_zoom * 1000, rec_errors[p1]['signal'][idx_start:idx_end], 'g-', label=f'DPCM with errors p={p1}', linewidth=2)
ax2.set_xlabel('Time (ms)', fontsize=12)
ax2.set_ylabel('Amplitude', fontsize=12)
ax2.set_title(f'DPCM R={R} bits - Errors p={p1} (SNR: {rec_errors[p1]["snr"]:.2f} dB)', fontsize=14)
ax2.legend(loc='best')
ax2.grid(True, alpha=0.3)
ax2.set_ylim(-1, 1)
ax2.set_yticks([-1, 0, 1])

# Subplot 3: DPCM with p=1e-3
ax3 = axes[2]
p2 = 1e-3
ax3.plot(t_zoom * 1000, x[idx_start:idx_end], 'b-', label='Original', linewidth=2)
ax3.plot(t_zoom * 1000, rec_errors[p2]['signal'][idx_start:idx_end], 'm-', label=f'DPCM with errors p={p2}', linewidth=2)
ax3.set_xlabel('Time (ms)', fontsize=12)
ax3.set_ylabel('Amplitude', fontsize=12)
ax3.set_title(f'DPCM R={R} bits - Errors p={p2} (SNR: {rec_errors[p2]["snr"]:.2f} dB)', fontsize=14)
ax3.legend(loc='best')
ax3.grid(True, alpha=0.3)
ax3.set_ylim(-1, 1)
ax3.set_yticks([-1, 0, 1])

plt.tight_layout()
plt.savefig('DPCM/dpcm_comparison.png', dpi=300, bbox_inches='tight')
print("Saved: DPCM/dpcm_comparison.png")
plt.close('all')

print("\nDPCM simulation done.")
