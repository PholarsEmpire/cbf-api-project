const BondsList = ({ bonds }) => {
  return (
    <div className="row">
      {bonds.map((bond) => {
        const {
          id,
          name = 'Unnamed Bond',
          issuer = 'Unknown',
          couponRate,
          maturityDate,
          price,
          imageUrl,
        } = bond

        const couponText =
          (couponRate ?? couponRate === 0) ? `${couponRate}%` : 'N/A'

        const maturityText = maturityDate
          ? new Date(maturityDate).toLocaleDateString()
          : 'N/A'

        const priceText =
          typeof price === 'number' ? `$${price.toFixed(2)}` : 'N/A'

        return (
          <div className="col-lg-4 col-md-6 col-sm-12 mb-4" key={id ?? Math.random()}>
            <div className="card h-100">
              <img
                src={imageUrl || 'https://placehold.co/600x400'}
                className="card-img-top"
                alt={name || 'Bond Image'}
              />
              <div className="card-body">
                <h5 className="card-title">{name}</h5>
                <p className="card-text"><strong>Issuer:</strong> {issuer}</p>
                <p className="card-text"><strong>Coupon Rate:</strong> {couponText}</p>
                <p className="card-text"><strong>Maturity Date:</strong> {maturityText}</p>
                <p className="card-text"><strong>Price:</strong> {priceText}</p>
                <a href="#" className="btn btn-primary">View Details</a>
              </div>
            </div>
          </div>
        )
      })}
    </div>
  )
}

export default BondsList
