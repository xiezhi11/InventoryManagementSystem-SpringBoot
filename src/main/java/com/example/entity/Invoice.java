package com.example.entity;

import java.io.Serializable;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the invoice database table.
 *
 */
@Entity
@NamedQuery(name="Invoice.findAll", query="SELECT i FROM Invoice i")
public class Invoice implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int invoiceId;

	private double lineTotal;

	private int productId;

	private String productName;

	private double quantity;

	private double total;

	private BigDecimal version;

	@Enumerated(EnumType.STRING)
	private InvoiceStatus status;

	@Temporal(TemporalType.TIMESTAMP)
	private Date confirmedAt;

	@Temporal(TemporalType.TIMESTAMP)
	private Date paidAt;

	@Temporal(TemporalType.TIMESTAMP)
	private Date voidedAt;

	private String statusRemark;

	@Column(name="CreatedUser", length=30)
	private String createdUser;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CreatedDateTime")
	private Date createdDateTime;

	@Column(name="LastModifiedUser", length=30)
	private String lastModifiedUser;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="LastModifiedDateTime")
	private Date lastModifiedDateTime;

	//bi-directional many-to-one association to ProductInvoice
	@OneToMany(mappedBy="invoice")
	private List<ProductInvoice> productInvoices;

	public Invoice() {
	}

	@PrePersist
	protected void onCreate() {
		if (this.status == null) {
			this.status = InvoiceStatus.DRAFT;
		}
		this.createdDateTime = new Date();
		this.lastModifiedDateTime = new Date();
	}

	@PreUpdate
	protected void onUpdate() {
		this.lastModifiedDateTime = new Date();
	}

	public int getInvoiceId() {
		return this.invoiceId;
	}

	public void setInvoiceId(int invoiceId) {
		this.invoiceId = invoiceId;
	}

	public double getLineTotal() {
		return this.lineTotal;
	}

	public void setLineTotal(double lineTotal) {
		this.lineTotal = lineTotal;
	}

	public int getProductId() {
		return this.productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return this.productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public double getQuantity() {
		return this.quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public double getTotal() {
		return this.total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public BigDecimal getVersion() {
		return this.version;
	}

	public void setVersion(BigDecimal version) {
		this.version = version;
	}

	public List<ProductInvoice> getProductInvoices() {
		return this.productInvoices;
	}

	public void setProductInvoices(List<ProductInvoice> productInvoices) {
		this.productInvoices = productInvoices;
	}

	public ProductInvoice addProductInvoice(ProductInvoice productInvoice) {
		getProductInvoices().add(productInvoice);
		productInvoice.setInvoice(this);

		return productInvoice;
	}

	public ProductInvoice removeProductInvoice(ProductInvoice productInvoice) {
		getProductInvoices().remove(productInvoice);
		productInvoice.setInvoice(null);

		return productInvoice;
	}

	public InvoiceStatus getStatus() {
		return this.status;
	}

	public void setStatus(InvoiceStatus status) {
		this.status = status;
	}

	public Date getConfirmedAt() {
		return this.confirmedAt;
	}

	public void setConfirmedAt(Date confirmedAt) {
		this.confirmedAt = confirmedAt;
	}

	public Date getPaidAt() {
		return this.paidAt;
	}

	public void setPaidAt(Date paidAt) {
		this.paidAt = paidAt;
	}

	public Date getVoidedAt() {
		return this.voidedAt;
	}

	public void setVoidedAt(Date voidedAt) {
		this.voidedAt = voidedAt;
	}

	public String getStatusRemark() {
		return this.statusRemark;
	}

	public void setStatusRemark(String statusRemark) {
		this.statusRemark = statusRemark;
	}

	public String getCreatedUser() {
		return this.createdUser;
	}

	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}

	public Date getCreatedDateTime() {
		return this.createdDateTime;
	}

	public void setCreatedDateTime(Date createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	public String getLastModifiedUser() {
		return this.lastModifiedUser;
	}

	public void setLastModifiedUser(String lastModifiedUser) {
		this.lastModifiedUser = lastModifiedUser;
	}

	public Date getLastModifiedDateTime() {
		return this.lastModifiedDateTime;
	}

	public void setLastModifiedDateTime(Date lastModifiedDateTime) {
		this.lastModifiedDateTime = lastModifiedDateTime;
	}

}
