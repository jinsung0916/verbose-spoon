package com.benope.verbose.spoon.web.product.domain.category

import com.benope.verbose.spoon.core_backend.common.jpa.BaseEntity
import com.benope.verbose.spoon.web.product.domain.brand.Brand
import com.benope.verbose.spoon.web.product.domain.product.Product
import com.benope.verbose.spoon.web.product.exception.CategoryCannotBeDeletedException
import org.hibernate.annotations.Where
import javax.persistence.*

@Entity
@Table(
    name = "category",
    uniqueConstraints = [UniqueConstraint(columnNames = ["brand", "category_name"])]
)
@Where(clause = BaseEntity.NOT_DELETED_CLAUSE)
open class Category protected constructor() : BaseEntity<Category>() {

    constructor(brand: Brand, categoryName: String) : this() {
        this.brand = brand
        this.categoryName = categoryName
    }

    @Id
    @Column(name = "category_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var categoryId: Long? = null
        protected set

    @Column(name = "brand")
    @Enumerated(EnumType.STRING)
    open var brand: Brand? = null
        protected set

    @Column(name = "category_name")
    open var categoryName: String? = null
        protected set

    // Entity 삭제 시 유효성 검사를 위해 제한적으로 어그리거트 간 접근을 허용했습니다.
    // 다른 로직에서 사용 시 표준적인 DDD 방법론을 이용하여 코딩해 주세요.
    @OneToMany(mappedBy = "categoryId")
    private var products: MutableSet<Product> = mutableSetOf()

    override fun markDeleted() {
        if (enableToDelete()) {
            super.markDeleted()
        } else {
            throw CategoryCannotBeDeletedException()
        }
    }

    private fun enableToDelete(): Boolean {
        return this.products.size == 0 || this.products.all { it.isDeleted }
    }

}