package com.benope.verbose.spoon.web.product.domain.product

import com.benope.verbose.spoon.core_backend.common.jpa.BaseEntity
import org.hibernate.annotations.Where
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "product")
@Where(clause = BaseEntity.NOT_DELETED_CLAUSE)
open class Product protected constructor() : BaseEntity<Product>() {

    constructor(productId: String, productName: String, categoryId: Long) : this() {
        this.productId = productId
        this.productName = productName
        this.categoryId = categoryId
    }

    @Id
    open var productId: String? = null
        protected set

    open var productName: String? = null
        protected set

    open var categoryId: Long? = null
        protected set

}