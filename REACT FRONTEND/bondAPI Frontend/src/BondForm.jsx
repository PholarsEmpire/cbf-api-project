// src/BondForm.jsx
import { useEffect, useState } from 'react';
import Modal from './Modal';

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
    if (initial) {
      setForm({
        id: initial.id ?? null,
        name: initial.name ?? '',
        issuer: initial.issuer ?? '',
        faceValue: initial.faceValue ?? '',
        couponRate: initial.couponRate ?? '',
        rating: initial.rating ?? '',
        issueDate: initial.issueDate ?? '',
        maturityDate: initial.maturityDate ?? '',
        status: initial.status ?? '',
        currency: initial.currency ?? '',
      });
    }
  }, [initial]);

  const update = (field, value) =>
    setForm((prev) => ({ ...prev, [field]: value }));

  const submit = (e) => {
    e.preventDefault();
    const payload = {
      ...form,
      faceValue: form.faceValue === '' ? null : Number(form.faceValue),
      couponRate: form.couponRate === '' ? null : Number(form.couponRate),
    };
    onSave?.(payload);
  };

  return (
    <Modal onClose={onCancel}>
      <h2 style={{ marginTop: 0 }}>
        {form.id ? 'Edit Bond' : 'Add Bond'}
      </h2>

      <form onSubmit={submit}>
        <div style={gridStyles}>
          <label style={labelStyles}>
            <span>Name</span>
            <input
              value={form.name}
              onChange={(e) => update('name', e.target.value)}
              required
            />
          </label>

          <label style={labelStyles}>
            <span>Issuer</span>
            <input
              value={form.issuer}
              onChange={(e) => update('issuer', e.target.value)}
              required
            />
          </label>

          <label style={labelStyles}>
            <span>Face Value</span>
            <input
              type="number"
              step="0.01"
              value={form.faceValue}
              onChange={(e) => update('faceValue', e.target.value)}
            />
          </label>

          <label style={labelStyles}>
            <span>Coupon Rate (%)</span>
            <input
              type="number"
              step="0.01"
              value={form.couponRate}
              onChange={(e) => update('couponRate', e.target.value)}
            />
          </label>

          <label style={labelStyles}>
            <span>Rating</span>
            <input
              value={form.rating}
              onChange={(e) => update('rating', e.target.value)}
            />
          </label>

          <label style={labelStyles}>
            <span>Issue Date</span>
            <input
              type="date"
              value={form.issueDate}
              onChange={(e) => update('issueDate', e.target.value)}
            />
          </label>

          <label style={labelStyles}>
            <span>Maturity Date</span>
            <input
              type="date"
              value={form.maturityDate}
              onChange={(e) => update('maturityDate', e.target.value)}
            />
          </label>

          <label style={labelStyles}>
            <span>Status</span>
            <input
              value={form.status}
              onChange={(e) => update('status', e.target.value)}
              placeholder="Active / Matured / Defaulted"
            />
          </label>

          <label style={labelStyles}>
            <span>Currency</span>
            <input
              value={form.currency}
              onChange={(e) => update('currency', e.target.value)}
              placeholder="USD / GBP / EUR"
            />
          </label>
        </div>

        <div style={actionsStyles}>
          <button type="button" className="btn ghost" onClick={onCancel}>
            Cancel
          </button>
          <button type="submit" className="btn primary">
            {form.id ? 'Save Changes' : 'Create Bond'}
          </button>
        </div>
      </form>
    </Modal>
  );
}

const gridStyles = {
  display: 'grid',
  gridTemplateColumns: 'repeat(2, minmax(0, 1fr))',
  gap: '16px',
};

const labelStyles = {
  display: 'flex',
  flexDirection: 'column',
  gap: '6px',
};

const actionsStyles = {
  marginTop: '16px',
  display: 'flex',
  gap: '12px',
  justifyContent: 'flex-end',
};
