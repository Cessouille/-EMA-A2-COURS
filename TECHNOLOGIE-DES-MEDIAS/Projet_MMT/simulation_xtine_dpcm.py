import numpy as np
import soundfile as sf
from scipy import signal
import matplotlib.pyplot as plt
import os

# Create output directories if they don't exist
os.makedirs('XTINE', exist_ok=True)

# Load the XTINE audio file
input_file = 'XTINE/xtineFs.wav'
x, Fs = sf.read(input_file)

print(f"Loaded: {input_file}")
print(f"Sample rate: {Fs} Hz")
print(f"Number of samples: {len(x)}")
print(f"Duration: {len(x)/Fs:.2f} seconds")

# Normalize to [-1, 1] range
x = x / np.max(np.abs(x))

# Time vector
t = np.arange(len(x)) / Fs

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
    if Pnoise == 0:
        return float('inf')
    return 10*np.log10(Psignal / Pnoise)


# DPCM Encoding
print("\n=== DPCM Encoding ===\n")
R = 8
print(f"DPCM with R={R} bits\n")
bits_dpcm, rec_dpcm = dpcm_encode(x, R)
snr_dpcm = snr_db(x, rec_dpcm)
print(f"SNR DPCM R={R}: {snr_dpcm:.2f} dB")
sf.write('XTINE/xtine_dpcm_R8.wav', rec_dpcm.astype(np.float32), Fs)

# Simulate errors for DPCM R=8 with p=1e-2
print("\n=== DPCM with Binary Errors ===\n")
p = 1e-2
print(f"Simulating binary errors p={p} on DPCM R={R} stream")
bits_stream, _ = dpcm_encode(x, R)
corrupted = bits_errors(bits_stream, p)
# Pass original signal to prevent error accumulation
rec_err = dpcm_decode(corrupted, R, len(x), original_signal=x)
snr_err = snr_db(x, rec_err)
print(f"SNR DPCM R={R} with p={p}: {snr_err:.2f} dB")
sf.write(f'XTINE/xtine_dpcm_R8_errors_p1e_02.wav', rec_err.astype(np.float32), Fs)

# Plot DPCM comparison with errors
print("\nGenerating DPCM comparison plot...")
# Show a zoomed section for better visibility (first 100ms)
t_start = 0.0
t_end = 0.1  # 100ms window to see details
idx_start = int(t_start * Fs)
idx_end = int(t_end * Fs)
t_zoom = t[idx_start:idx_end]

# Create figure with 3 subplots
fig, axes = plt.subplots(3, 1, figsize=(14, 12))
fig.suptitle('DPCM on XTINE Signal with Binary Errors (100ms window)', fontsize=16)

# Subplot 1: Original signal only
ax1 = axes[0]
ax1.plot(t_zoom * 1000, x[idx_start:idx_end], 'b-', label='Original', linewidth=1.5)
ax1.set_xlabel('Time (ms)', fontsize=12)
ax1.set_ylabel('Amplitude', fontsize=12)
ax1.set_title(f'Original XTINE Signal', fontsize=14)
ax1.legend(loc='best')
ax1.grid(True, alpha=0.3)
ax1.set_ylim(-1, 1)
ax1.set_yticks([-1, 0, 1])

# Subplot 2: DPCM without errors
ax2 = axes[1]
ax2.plot(t_zoom * 1000, rec_dpcm[idx_start:idx_end], 'r-', label=f'DPCM R={R} bits (no errors)', linewidth=1.5)
ax2.set_xlabel('Time (ms)', fontsize=12)
ax2.set_ylabel('Amplitude', fontsize=12)
ax2.set_title(f'DPCM R={R} bits - No Errors (SNR: {snr_dpcm:.2f} dB)', fontsize=14)
ax2.legend(loc='best')
ax2.grid(True, alpha=0.3)
ax2.set_ylim(-1, 1)
ax2.set_yticks([-1, 0, 1])

# Subplot 3: DPCM with p=1e-2
ax3 = axes[2]
ax3.plot(t_zoom * 1000, rec_err[idx_start:idx_end], 'g-', label=f'DPCM with errors p={p}', linewidth=1.5)
ax3.set_xlabel('Time (ms)', fontsize=12)
ax3.set_ylabel('Amplitude', fontsize=12)
ax3.set_title(f'DPCM R={R} bits - Errors p={p} (SNR: {snr_err:.2f} dB)', fontsize=14)
ax3.legend(loc='best')
ax3.grid(True, alpha=0.3)
ax3.set_ylim(-1, 1)
ax3.set_yticks([-1, 0, 1])

plt.tight_layout()
plt.savefig('XTINE/xtine_dpcm_comparison.png', dpi=300, bbox_inches='tight')
print("Saved: XTINE/xtine_dpcm_comparison.png")
plt.close('all')

print("\nXTINE DPCM simulation done.")
