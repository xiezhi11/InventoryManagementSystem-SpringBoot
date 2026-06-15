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
	private Date createdDateTime;

	@Temporal(TemporalType.TIMESTAMP)
	private Date confirmedDateTime;

	@Temporal(TemporalType.TIMESTAMP)
	private Date paidDateTime;

	@Temporal(TemporalType.TIMESTAMP)
	private Date voidedDateTime;

	//bi-directional many-to-one association to ProductInvoice
	@OneToMany(mappedBy="invoice")
	private List<ProductInvoice> productInvoices;

	public Invoice() {
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

	public InvoiceStatus getStatus() {
		return this.status;
	}

	public void setStatus(InvoiceStatus status) {
		this.status = status;
	}

	public Date getCreatedDateTime() {
		return this.createdDateTime;
	}

	public void setCreatedDateTime(Date createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	public Date getConfirmedDateTime() {
		return this.confirmedDateTime;
	}

	public void setConfirmedDateTime(Date confirmedDateTime) {
		this.confirmedDateTime = confirmedDateTime;
	}

	public Date getPaidDateTime() {
		return this.paidDateTime;
	}

	public void setPaidDateTime(Date paidDateTime) {
		this.paidDateTime = paidDateTime;
	}

	public Date getVoidedDateTime() {
		return this.voidedDateTime;
	}

	public void setVoidedDateTime(Date voidedDateTime) {
		this.voidedDateTime = voidedDateTime;
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

}