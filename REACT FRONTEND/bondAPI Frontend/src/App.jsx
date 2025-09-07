import { useEffect, useMemo, useState } from 'react'
import './App.css'
import BondsTable from './BondsTable'
import BondForm from './BondForm'
import ExternalPanel from './ExternalPanel'

export default function App() {
  const [bonds, setBonds] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  const [q, setQ] = useState('')
  const [sortBy, setSortBy] = useState('maturityAsc')

  const [issuer, setIssuer] = useState('')
  const [rating, setRating] = useState('')
  const [status, setStatus] = useState('')
  const [currency, setCurrency] = useState('')

  const [couponMin, setCouponMin] = useState('')
  const [couponMax, setCouponMax] = useState('')

  const [faceMin, setFaceMin] = useState('')
  const [faceMax, setFaceMax] = useState('')

  const [maturityAfter, setMaturityAfter] = useState('')
  const [maturityStart, setMaturityStart] = useState('')
  const [maturityEnd, setMaturityEnd] = useState('')

  const [issueAfter, setIssueAfter] = useState('')
  const [issueStart, setIssueStart] = useState('')
  const [issueEnd, setIssueEnd] = useState('')

  const [showForm, setShowForm] = useState(false)
  const [editingBond, setEditingBond] = useState(null)

  const fetchJson = async (path) => {
    setLoading(true)
    try {
      const res = await fetch(path)
      if (!res.ok) throw new Error(`${res.status} ${res.statusText}`)
      const data = await res.json()
      setBonds(Array.isArray(data) ? data : [])
      setError(null)
    } catch (e) {
      setError(e.message || 'Failed to load bonds')
      setBonds([])
    } finally {
      setLoading(false)
    }
  }

  const fetchAll = () => fetchJson('/api/bonds')

  useEffect(() => {
    let ignore = false
    ;(async () => {
      setLoading(true)
      try {
        const res = await fetch('/api/bonds')
        if (!res.ok) throw new Error(`${res.status} ${res.statusText}`)
        const data = await res.json()
        if (!ignore) {
          setBonds(Array.isArray(data) ? data : [])
          setError(null)
        }
      } catch (e) {
        if (!ignore) setError(e.message || 'Failed to load bonds')
      } finally {
        if (!ignore) setLoading(false)
      }
    })()
    return () => { ignore = true }
  }, [])

  const issuers = useMemo(
    () => Array.from(new Set(bonds.map(b => b.issuer).filter(Boolean))),
    [bonds]
  )
  const ratings = useMemo(
    () => Array.from(new Set(bonds.map(b => b.rating).filter(Boolean))),
    [bonds]
  )
  const statuses = useMemo(
    () => Array.from(new Set(bonds.map(b => b.status).filter(Boolean))),
    [bonds]
  )
  const currencies = useMemo(
    () => Array.from(new Set(bonds.map(b => b.currency).filter(Boolean))),
    [bonds]
  )

  const filtered = useMemo(() => {
    let list = [...bonds]
    if (q.trim()) {
      const s = q.toLowerCase()
      list = list.filter(b =>
        (b.name ?? '').toLowerCase().includes(s) ||
        (b.issuer ?? '').toLowerCase().includes(s) ||
        (b.rating ?? '').toLowerCase().includes(s) ||
        (b.status ?? '').toLowerCase().includes(s) ||
        (b.currency ?? '').toLowerCase().includes(s)
      )
    }
    if (currency) list = list.filter(b => b.currency === currency)

    const num = v => (typeof v === 'number' ? v : -Infinity)
    const dateNum = d => (d ? new Date(d).getTime() : 0)
    switch (sortBy) {
      case 'priceAsc': list.sort((a, b) => num(a.faceValue) - num(b.faceValue)); break
      case 'priceDesc': list.sort((a, b) => num(b.faceValue) - num(a.faceValue)); break
      case 'couponAsc': list.sort((a, b) => num(a.couponRate) - num(b.couponRate)); break
      case 'couponDesc': list.sort((a, b) => num(b.couponRate) - num(a.couponRate)); break
      case 'maturityAsc': list.sort((a, b) => dateNum(a.maturityDate) - dateNum(b.maturityDate)); break
      case 'maturityDesc': list.sort((a, b) => dateNum(b.maturityDate) - dateNum(a.maturityDate)); break
      case 'issueAsc': list.sort((a, b) => dateNum(a.issueDate) - dateNum(b.issueDate)); break
      case 'issueDesc': list.sort((a, b) => dateNum(b.issueDate) - dateNum(a.issueDate)); break
      default: break
    }
    return list
  }, [bonds, q, currency, sortBy])

  const applyServerFilters = async () => {
    if (issuer) return fetchJson(`/api/bonds/issuer/${encodeURIComponent(issuer)}`)
    if (rating) return fetchJson(`/api/bonds/rating/${encodeURIComponent(rating)}`)
    if (status) {
      const params = new URLSearchParams({ status })
      return fetchJson(`/api/bonds/status?${params.toString()}`)
    }
    if (couponMin !== '' && couponMax !== '') {
      return fetchJson(`/api/bonds/coupon-rate/${encodeURIComponent(couponMin)}/${encodeURIComponent(couponMax)}`)
    }
    if (couponMin !== '') {
      return fetchJson(`/api/bonds/coupon-rate/${encodeURIComponent(couponMin)}`)
    }
    if (faceMin !== '' && faceMax !== '') {
      const params = new URLSearchParams({ 'min-value': faceMin, 'max-value': faceMax })
      return fetchJson(`/api/bonds/face-value-between?${params.toString()}`)
    }
    if (faceMin !== '') {
      return fetchJson(`/api/bonds/face-value/${encodeURIComponent(faceMin)}`)
    }
    if (maturityStart && maturityEnd) {
      return fetchJson(`/api/bonds/maturing-between/${encodeURIComponent(maturityStart)}/${encodeURIComponent(maturityEnd)}`)
    }
    if (maturityAfter) {
      return fetchJson(`/api/bonds/maturity-date/${encodeURIComponent(maturityAfter)}`)
    }
    if (issueStart && issueEnd) {
      const params = new URLSearchParams({ 'start-date': issueStart, 'end-date': issueEnd })
      return fetchJson(`/api/bonds/issued-between?${params.toString()}`)
    }
    if (issueAfter) {
      return fetchJson(`/api/bonds/issue-date/${encodeURIComponent(issueAfter)}`)
    }
    return fetchAll()
  }

  const openCreate = () => { setEditingBond(null); setShowForm(true) }
  const openEdit = (bond) => { setEditingBond(bond); setShowForm(true) }

  const remove = async (bond) => {
    if (!confirm(`Delete bond "${bond.name}"?`)) return
    const res = await fetch(`/api/bonds/${bond.id}`, { method: 'DELETE' })
    if (!res.ok) { alert('Failed to delete'); return }
    await fetchAll()
  }

  const save = async (payload) => {
    const isEdit = !!payload.id
    const url = isEdit ? `/api/bonds/${payload.id}` : '/api/bonds'
    const method = isEdit ? 'PUT' : 'POST'
    const res = await fetch(url, {
      method,
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload),
    })
    if (!res.ok) { alert(`Failed to ${isEdit ? 'update' : 'create'} bond`); return }
    setShowForm(false)
    await fetchAll()
  }

  const clearServerFilters = () => {
    setIssuer(''); setRating(''); setStatus(''); setCurrency('')
    setCouponMin(''); setCouponMax('')
    setFaceMin(''); setFaceMax('')
    setMaturityAfter(''); setMaturityStart(''); setMaturityEnd('')
    setIssueAfter(''); setIssueStart(''); setIssueEnd('')
    fetchAll()
  }

  return (
    <div className="app">
      <header className="app-header"><h1>Bond (Fixed-Income Investment) Catalog</h1></header>

      <section className="toolbar container">
        <div className="filters">
          <input
            className="input"
            placeholder="Search name / issuer / rating / status / currency"
            value={q}
            onChange={e => setQ(e.target.value)}
          />

          <select className="select" value={currency} onChange={e => setCurrency(e.target.value)}>
            <option value="">All currencies</option>
            {currencies.map(c => <option key={c} value={c}>{c}</option>)}
          </select>

          <select className="select" value={sortBy} onChange={e => setSortBy(e.target.value)}>
            <option value="maturityAsc">Maturity: Soonest</option>
            <option value="maturityDesc">Maturity: Farthest</option>
            <option value="issueAsc">Issue: Oldest</option>
            <option value="issueDesc">Issue: Newest</option>
            <option value="couponDesc">Coupon: High → Low</option>
            <option value="couponAsc">Coupon: Low → High</option>
            <option value="priceDesc">Face Value: High → Low</option>
            <option value="priceAsc">Face Value: Low → High</option>
          </select>

          <button className="btn primary" onClick={openCreate}>+ Add Bond</button>
        </div>
      </section>

      <section className="container card pad">
        <h3 className="section-title">Filters:</h3>
        <div className="grid-4">
          <label><span>Issuer:</span>
            <input className="input" list="issuer-list" value={issuer} onChange={e => setIssuer(e.target.value)} />
            <datalist id="issuer-list">{issuers.map(i => <option key={i} value={i} />)}</datalist>
          </label>

          <label><span>Rating:</span>
            <input className="input" list="rating-list" value={rating} onChange={e => setRating(e.target.value)} />
            <datalist id="rating-list">{ratings.map(r => <option key={r} value={r} />)}</datalist>
          </label>

          <label><span>Status:</span>
            <input className="input" list="status-list" value={status} onChange={e => setStatus(e.target.value)} />
            <datalist id="status-list">{statuses.map(s => <option key={s} value={s} />)}</datalist>
          </label>

          <label><span>Currency</span>
            <input className="input" list="currency-list" value={currency} onChange={e => setCurrency(e.target.value)} />
            <datalist id="currency-list">{currencies.map(c => <option key={c} value={c} />)}</datalist>
          </label>

          <label><span>Coupon ≥</span>
            <input className="input" type="number" step="0.01" value={couponMin} onChange={e => setCouponMin(e.target.value)} />
          </label>

          <label><span>Coupon ≤</span>
            <input className="input" type="number" step="0.01" value={couponMax} onChange={e => setCouponMax(e.target.value)} />
          </label>

          <label><span>Face Value ≥</span>
            <input className="input" type="number" step="0.01" value={faceMin} onChange={e => setFaceMin(e.target.value)} />
          </label>

          <label><span>Face Value ≤</span>
            <input className="input" type="number" step="0.01" value={faceMax} onChange={e => setFaceMax(e.target.value)} />
          </label>

          <label><span>Maturity After</span>
            <input className="input" type="date" value={maturityAfter} onChange={e => setMaturityAfter(e.target.value)} />
          </label>

          <label><span>Maturity Start</span>
            <input className="input" type="date" value={maturityStart} onChange={e => setMaturityStart(e.target.value)} />
          </label>

          <label><span>Maturity End</span>
            <input className="input" type="date" value={maturityEnd} onChange={e => setMaturityEnd(e.target.value)} />
          </label>

          <label><span>Issue After</span>
            <input className="input" type="date" value={issueAfter} onChange={e => setIssueAfter(e.target.value)} />
          </label>

          <label><span>Issue Start</span>
            <input className="input" type="date" value={issueStart} onChange={e => setIssueStart(e.target.value)} />
          </label>

          <label><span>Issue End</span>
            <input className="input" type="date" value={issueEnd} onChange={e => setIssueEnd(e.target.value)} />
          </label>
        </div>

        <div className="actions-right">
          <button className="btn secondary" onClick={applyServerFilters}>Apply Filters (API)</button>
          <button className="btn ghost" onClick={clearServerFilters}>Clear & Load All</button>
        </div>
      </section>

      {!loading && !error && (
        <div className="container">
          <ExternalPanel bonds={bonds} />
        </div>
      )}

      <main className="container">
        {loading && <p className="muted">Loading…</p>}
        {error && <p className="error">Error: {error}</p>}

        {!loading && !error && (
          <>
            {filtered.length ? (
              <>
                <p className="muted">Showing {filtered.length} of {bonds.length} bonds.</p>
                <BondsTable bonds={filtered} onEdit={openEdit} onDelete={remove} />
              </>
            ) : (
              <p className="muted">No bonds match current view.</p>
            )}
          </>
        )}
      </main>

      {showForm && (
        <BondForm
          initial={editingBond}
          onCancel={() => setShowForm(false)}
          onSave={save}
        />
      )}
    </div>
  )
}
