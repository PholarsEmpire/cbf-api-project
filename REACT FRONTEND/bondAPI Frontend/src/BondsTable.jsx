
export default function BondsTable({ bonds, onEdit, onDelete }) {
  const dd = d => d ? new Date(d).toLocaleDateString() : 'N/A'
  const money = v => (typeof v === 'number' ? v.toLocaleString(undefined, { maximumFractionDigits: 2 }) : 'N/A')
  const pct = v => (typeof v === 'number' ? `${v}%` : 'N/A')

  return (
    <div className="table-wrap">
      <table className="table wide">
        <thead>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Issuer</th>
            <th>Face Value</th>
            <th>Coupon</th>
            <th>Rating</th>
            <th>Issue Date</th>
            <th>Maturity Date</th>
            <th>Status</th>
            <th>Currency</th>
            <th style={{width: 150}}>Actions</th>
          </tr>
        </thead>
        <tbody>
          {bonds.map(b => (
            <tr key={b.id}>
              <td className="mono">{b.id}</td>
              <td>{b.name || 'Unnamed Bond'}</td>
              <td>{b.issuer || 'Unknown'}</td>
              <td className="mono">{money(b.faceValue)}</td>
              <td>{pct(b.couponRate)}</td>
              <td>{b.rating || 'N/A'}</td>
              <td className="mono">{dd(b.issueDate)}</td>
              <td className="mono">{dd(b.maturityDate)}</td>
              <td><span className={`status-pill ${b.status ? b.status.toLowerCase() : ''}`}>{b.status || 'N/A'}</span></td>
              <td>{b.currency || 'N/A'}</td>
              <td className="actions">
                <button className="btn small ghost" onClick={() => onEdit(b)}>Edit</button>
                <button className="btn small danger" onClick={() => onDelete(b)}>Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}
