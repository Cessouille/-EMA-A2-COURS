import numpy as np
import soundfile as sf
from scipy import signal
import matplotlib.pyplot as plt

# Parametres signaux
f0 = 2000.0  # 2 kHz
T = 3.0      # 3 seconds
samples_per_period = 10  # 10 echantillons par periode
Fs = int(f0 * samples_per_period)  # frequence d'echantillonnage

print(f"Fs = {Fs} Hz")

t = np.arange(0, T, 1.0/Fs)
# Signal de reference (sinus)
x = 0.9 * np.sin(2*np.pi*f0*t)  # amplitude 0.9 pour eviter saturation

# Fonctions utilitaires

def quantize(x, bits):
    if bits <= 0:
        # 1-bit equiv : signe
        return (x >= 0).astype(np.int8)
    levels = 2 ** bits
    # uniform mid-rise quantizer between -1 and 1
    step = 2.0 / levels
    q = np.floor((x + 1.0) / step).astype(np.int32)
    q = np.clip(q, 0, levels-1)
    # reconstruction
    xr = (q + 0.5) * step - 1.0
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
    step = 2.0 / (2**bits)
    xr = (q + 0.5) * step - 1.0
    return xr


def dpcm_encode(x, R):
    # encode differences using R bits
    ns = len(x)
    pred = 0.0
    diffs = np.zeros(ns)
    qlevels = 2**R
    step = 2.0 / qlevels
    qindex = np.zeros(ns, dtype=np.int32)
    rec = np.zeros(ns)
    for n in range(ns):
        e = x[n] - pred
        idx = int(np.floor((e + 1.0) / step))
        idx = np.clip(idx, 0, qlevels-1)
        qindex[n] = idx
        rec_e = (idx + 0.5)*step - 1.0
        rec[n] = rec_e + pred
        pred = rec[n]
    # bits stream
    bits_stream = ((qindex[:, None] & (1 << np.arange(R))) > 0).astype(np.uint8).flatten()
    return bits_stream, rec


def dpcm_decode(bits_stream, R, nsamples):
    qlevels = 2**R
    step = 2.0 / qlevels
    bs = bits_stream.reshape(nsamples, R)
    qindex = np.zeros(nsamples, dtype=np.int32)
    for i in range(R):
        qindex |= (bs[:, i].astype(np.int32) << i)
    pred = 0.0
    rec = np.zeros(nsamples)
    for n in range(nsamples):
        rec_e = (qindex[n] + 0.5)*step - 1.0
        rec[n] = pred + rec_e
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


# Partie 1: PCM
bits_list = [8, 6, 4, 3, 2, 1]
results = {}

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
    print(f"SNR PCM {b} bits: {s:.2f} dB")
    # save WAV (scaled to int16)
    sf.write(f'pcm_{b}bits.wav', (rec*0.9).astype(np.float32), Fs)

# special: what happens at 1 bit handled above

# Partie 2: DPCM
R = 8
print(f"\nDPCM with R={R} bits\n")
bits_dpcm, rec_dpcm = dpcm_encode(x, R)
print(f"SNR DPCM R={R}: {snr_db(x, rec_dpcm):.2f} dB")
sf.write('dpcm_R8.wav', (rec_dpcm*0.9).astype(np.float32), Fs)

# Simulate errors for PCM 8 bits
for p in [1e-2, 1e-3]:
    print(f"\nSimulating binary errors p={p} on PCM 8-bit stream")
    bits_stream, _ = pcm_encode(x, 8)
    corrupted = bits_errors(bits_stream, p)
    rec_err = pcm_decode(corrupted.reshape(-1), 8, len(x))
    print(f"SNR PCM 8 bits with p={p}: {snr_db(x, rec_err):.2f} dB")

# Simulate errors for DPCM R=8
for p in [1e-2, 1e-3]:
    print(f"\nSimulating binary errors p={p} on DPCM R={R} stream")
    bits_stream, _ = dpcm_encode(x, R)
    corrupted = bits_errors(bits_stream, p)
    rec_err = dpcm_decode(corrupted, R, len(x))
    print(f"SNR DPCM R={R} with p={p}: {snr_db(x, rec_err):.2f} dB")

print("\nDone.")
