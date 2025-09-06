const StatusFilter = ({ statuses = [], value, onChange }) => {
  return (
    <select
      id="statusFilter"
      className="form-select"
      value={value ?? ''}                    // controlled
      onChange={(e) => onChange(e.target.value || null)}
    >
      <option value="">All statuses</option>
      {statuses.map((s) => (
        <option key={s} value={s}>
          {s}
        </option>
      ))}
    </select>
  );
};

export default StatusFilter;
