import { useEffect, useState } from 'react'

// normalize anything (string/number/Date/object) into YYYY-MM-DD or ''
function toISODate(v) {
  if (!v) return '';
  if (typeof v === 'string') {
    // already ok?
    if (/^\d{4}-\d{2}-\d{2}$/.test(v)) return v;
    // try to parse other string formats
    const d = new Date(v);
    return isNaN(d.getTime()) ? '' : d.toISOString().slice(0, 10);
  }
  if (v instanceof Date) return isNaN(v.getTime()) ? '' : v.toISOString().slice(0, 10);
  try {
    const d = new Date(v);
    return isNaN(d.getTime()) ? '' : d.toISOString().slice(0, 10);
  } catch {
    return '';
  }
}

export default function BondForm({ initial, onCancel, onSave }) {
  const [form, setForm] = useState({
    id: null,
    name: '',
    issuer: '',
    faceValue: '',
    couponRate: '',
    rating: '',
    issueDate: '',
    maturityDate: '',
    status: '',
    currency: '',
  });

  useEffect(() => {
    if (!initial) return;
    setForm({
      id: initial.id ?? null,
      name: String(initial.name ?? ''),
      issuer: String(initial.issuer ?? ''),
      // Inputs want string or number; we use string for safety
      faceValue: initial.faceValue ?? '',
      couponRate: initial.couponRate ?? '',
      rating: String(initial.rating ?? ''),
      issueDate: toISODate(initial.issueDate),
      maturityDate: toISODate(initial.maturityDate),
      status: String(initial.status ?? ''),
      currency: String(initial.currency ?? ''),
    });
  }, [initial]);

  const update = (k, v) => setForm(f => ({ ...f, [k]: v }));

  const submit = (e) => {
    e.preventDefault();
    // Convert back to numbers where appropriate (empty -> null)
    const payload = {
      id: form.id,
      name: form.name.trim(),
      issuer: form.issuer.trim(),
      faceValue: form.faceValue === '' ? null : Number(form.faceValue),
      couponRate: form.couponRate === '' ? null : Number(form.couponRate),
      rating: form.rating.trim() || null,
      issueDate: form.issueDate || null,       // backend expects ISO string yyyy-MM-dd
      maturityDate: form.maturityDate || null, // same
      status: form.status.trim() || null,
      currency: form.currency.trim() || null,
    };
    onSave(payload);
  };

  return (
    <div className="modal-backdrop" onClick={onCancel}>
      <div className="modal" onClick={(e) => e.stopPropagation()}>
        <h2>{form.id ? 'Edit Bond' : 'Add Bond'}</h2>

        <form onSubmit={submit} className="form">
          <div className="grid">
            <label><span>Name</span>
              <input value={form.name} onChange={e=>update('name', e.target.value)} required />
            </label>

            <label><span>Issuer</span>
              <input value={form.issuer} onChange={e=>update('issuer', e.target.value)} required />
            </label>

            <label><span>Face Value</span>
              <input type="number" step="0.01"
                     value={String(form.faceValue)}
                     onChange={e=>update('faceValue', e.target.value)} />
            </label>

            <label><span>Coupon Rate (%)</span>
              <input type="number" step="0.01"
                     value={String(form.couponRate)}
                     onChange={e=>update('couponRate', e.target.value)} />
            </label>

            <label><span>Rating</span>
              <input value={form.rating} onChange={e=>update('rating', e.target.value)} />
            </label>

            <label><span>Issue Date</span>
              <input type="date" value={form.issueDate} onChange={e=>update('issueDate', e.target.value)} />
            </label>

            <label><span>Maturity Date</span>
              <input type="date" value={form.maturityDate} onChange={e=>update('maturityDate', e.target.value)} />
            </label>

            <label><span>Status</span>
              <input value={form.status} onChange={e=>update('status', e.target.value)} placeholder="Active / Matured / Defaulted" />
            </label>

            <label><span>Currency</span>
              <input value={form.currency} onChange={e=>update('currency', e.target.value)} placeholder="USD / GBP / EUR" />
            </label>
          </div>

          <div className="modal-actions">
            <button type="button" className="btn ghost" onClick={onCancel}>Cancel</button>
            <button type="submit" className="btn primary">{form.id ? 'Save Changes' : 'Create Bond'}</button>
          </div>
        </form>
      </div>
    </div>
  );
}
