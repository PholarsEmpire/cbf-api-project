
import { useState } from 'react';

export default function ExternalPanel({ bonds = [] }) {
  // FX pair
  const [fromCcy, setFromCcy] = useState('USD');
  const [toCcy, setToCcy] = useState('NGN');
  const [fxRate, setFxRate] = useState(null);
  const [fxLoading, setFxLoading] = useState(false);
  const [fxErr, setFxErr] = useState(null);

  const getFx = async () => {
    setFxLoading(true); setFxErr(null); setFxRate(null);
    try {
      const res = await fetch(`/api/external/fx?from=${fromCcy}&to=${toCcy}`);
      if (!res.ok) throw new Error(`${res.status} ${res.statusText}`);
      const rate = await res.json();
      setFxRate(rate);
    } catch (e) {
      setFxErr(e.message || 'Failed to load FX rate');
    } finally {
      setFxLoading(false);
    }
  };

  // Bond value conversion
  const [bondId, setBondId] = useState('');
  const [targetCcy, setTargetCcy] = useState('USD');
  const [conv, setConv] = useState(null);
  const [convLoading, setConvLoading] = useState(false);
  const [convErr, setConvErr] = useState(null);

  const convertBond = async () => {
    if (!bondId) return;
    setConvLoading(true); setConvErr(null); setConv(null);
    try {
      const res = await fetch(`/api/external/bonds/${bondId}/value-in?currency=${targetCcy}`);
      if (!res.ok) throw new Error(`${res.status} ${res.statusText}`);
      const data = await res.json();
      setConv(data);
    } catch (e) {
      setConvErr(e.message || 'Conversion failed');
    } finally {
      setConvLoading(false);
    }
  };

  // Macro (World Bank)
  const [iso, setIso] = useState('USA');    // ISO3: USA, NGA, GBR…
  const [year, setYear] = useState('2022');
  const [gdp, setGdp] = useState(null);
  const [infl, setInfl] = useState(null);
  const [macroErr, setMacroErr] = useState(null);
  const [macroLoading, setMacroLoading] = useState(false);

  const loadGdp = async () => {
    setMacroLoading(true); setMacroErr(null);
    try {
      const res = await fetch(`/api/external/macro/${iso}/gdp?year=${year}`);
      if (!res.ok) throw new Error(`${res.status} ${res.statusText}`);
      setGdp(await res.json());
    } catch (e) {
      setMacroErr(e.message || 'GDP fetch failed');
    } finally {
      setMacroLoading(false);
    }
  };

  const loadInflation = async () => {
    setMacroLoading(true); setMacroErr(null);
    try {
      const res = await fetch(`/api/external/macro/${iso}/inflation?year=${year}`);
      if (!res.ok) throw new Error(`${res.status} ${res.statusText}`);
      setInfl(await res.json());
    } catch (e) {
      setMacroErr(e.message || 'Inflation fetch failed');
    } finally {
      setMacroLoading(false);
    }
  };

  return (
    <div className="card mb-4 border-0 shadow-sm">
      <div className="card-body">
        <h2 className="h4 mb-3" style={{color:'#1e3a8a'}}>Market Data</h2>

        {/* FX Converter */}
        <div className="mb-4 p-3 rounded" style={{background:'#fef9c3'}}>
          <h3 className="h6 mb-3">FX Converter</h3>
          <div className="row g-2 align-items-end">
            <div className="col-sm-3">
              <label className="form-label">From</label>
              <input className="form-control" value={fromCcy} onChange={e=>setFromCcy(e.target.value.toUpperCase())} placeholder="USD" />
            </div>
            <div className="col-sm-3">
              <label className="form-label">To</label>
              <input className="form-control" value={toCcy} onChange={e=>setToCcy(e.target.value.toUpperCase())} placeholder="NGN" />
            </div>
            <div className="col-sm-3">
              <button className="btn primary w-100" onClick={getFx} disabled={fxLoading}>
                {fxLoading ? 'Fetching…' : 'Get Rate'}
              </button>
            </div>
            <div className="col-sm-3">
              {fxErr && <div className="text-danger small">{fxErr}</div>}
              {fxRate !== null && (
                <div className="fw-semibold">1 {fromCcy} = <span style={{color:'#1e3a8a'}}>{fxRate}</span> {toCcy}</div>
              )}
            </div>
          </div>
        </div>

        {/* Bond Face Value Conversion */}
        <div className="mb-4 p-3 rounded" style={{background:'#eff6ff'}}>
          <h3 className="h6 mb-3">Bond Face Value → Currency</h3>
          <div className="row g-2 align-items-end">
            <div className="col-md-5">
              <label className="form-label">Bond</label>
              <select className="form-select" value={bondId} onChange={e=>setBondId(e.target.value)}>
                <option value="">Select a bond…</option>
                {bonds.map(b => (
                  <option key={b.id} value={b.id}>
                    #{b.id} — {b.name} ({b.currency || '—'})
                  </option>
                ))}
              </select>
            </div>
            <div className="col-md-3">
              <label className="form-label">Target Currency</label>
              <input className="form-control" value={targetCcy} onChange={e=>setTargetCcy(e.target.value.toUpperCase())} placeholder="USD" />
            </div>
            <div className="col-md-2">
              <button className="btn primary w-100" onClick={convertBond} disabled={!bondId || convLoading}>
                {convLoading ? 'Converting…' : 'Convert'}
              </button>
            </div>
            <div className="col-md-2">
              {convErr && <div className="text-danger small">{convErr}</div>}
            </div>
          </div>

          {conv && (
            <div className="mt-3 small">
              <div><strong>{conv.bondName}</strong> (ID: {conv.bondId})</div>
              <div>Rate: 1 {conv.fromCurrency} → {conv.rate} {conv.toCurrency}</div>
              <div>Original Face: {String(conv.originalFaceValue)} {conv.fromCurrency}</div>
              <div>Converted Face: <span className="fw-semibold" style={{color:'#1e3a8a'}}>{String(conv.convertedFaceValue)} {conv.toCurrency}</span></div>
            </div>
          )}
        </div>

        {/* World Bank Macro */}
        <div className="p-3 rounded" style={{background:'#f8fafc'}}>
          <h3 className="h6 mb-3">World Bank — GDP & Inflation</h3>
          <div className="row g-2 align-items-end">
            <div className="col-sm-3">
              <label className="form-label">Country ISO (e.g. USA, NGA)</label>
              <input className="form-control" value={iso} onChange={e=>setIso(e.target.value.toUpperCase())} />
            </div>
            <div className="col-sm-2">
              <label className="form-label">Year</label>
              <input className="form-control" value={year} onChange={e=>setYear(e.target.value)} />
            </div>
            <div className="col-sm-3 d-grid">
              <button className="btn primary" onClick={loadGdp} disabled={macroLoading}>Get GDP</button>
            </div>
            <div className="col-sm-3 d-grid">
              <button className="btn ghost" onClick={loadInflation} disabled={macroLoading}>Get Inflation</button>
            </div>
          </div>
          {macroErr && <div className="text-danger small mt-2">{macroErr}</div>}

          <div className="row mt-3 g-3">
            {gdp && (
              <div className="col-md-6">
                <div className="p-3 rounded border">
                  <div className="fw-semibold">GDP ({gdp.year}) — {gdp.country}</div>
                  <div className="small text-muted">{gdp.indicator}</div>
                  <div className="fs-5 mt-1" style={{color:'#1e3a8a'}}>
                    {Number(gdp.value || 0).toLocaleString('en-US', {style:'currency', currency:'USD', maximumFractionDigits:0})}
                  </div>
                </div>
              </div>
            )}
            {infl && (
              <div className="col-md-6">
                <div className="p-3 rounded border">
                  <div className="fw-semibold">Inflation ({infl.year}) — {infl.country}</div>
                  <div className="small text-muted">{infl.indicator}</div>
                  <div className="fs-5 mt-1" style={{color:'#1e3a8a'}}>
                    {infl.value ?? 'N/A'}%
                  </div>
                </div>
              </div>
            )}
          </div>
        </div>

      </div>
    </div>
  );
}
