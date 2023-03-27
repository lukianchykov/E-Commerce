// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)

package com.gbsfo.ecommerce.utils.identifiable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class IdentifiableEntity {

    @Id
    @GeneratedValue(
        strategy = GenerationType.IDENTITY
    )
    private Long id;

    public IdentifiableEntity() {
    }

    protected IdentifiableEntity(IdentifiableEntityBuilder<?, ?> b) {
        this.id = b.id;
    }

    public static IdentifiableEntityBuilder<?, ?> builder() {
        return new IdentifiableEntityBuilderImpl();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof IdentifiableEntity)) {
            return false;
        } else {
            IdentifiableEntity other = (IdentifiableEntity) o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$id = this.getId();
                Object other$id = other.getId();
                if (this$id == null) {
                    if (other$id != null) {
                        return false;
                    }
                } else if (!this$id.equals(other$id)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof IdentifiableEntity;
    }

    public int hashCode() {
        int result = 1;
        Object $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        return result;
    }

    public String toString() {
        return "IdentifiableEntity(id=" + this.getId() + ")";
    }

    private static final class IdentifiableEntityBuilderImpl extends IdentifiableEntityBuilder<IdentifiableEntity, IdentifiableEntityBuilderImpl> {

        private IdentifiableEntityBuilderImpl() {
        }

        protected IdentifiableEntityBuilderImpl self() {
            return this;
        }

        public IdentifiableEntity build() {
            return new IdentifiableEntity(this);
        }
    }

    public abstract static class IdentifiableEntityBuilder<C extends IdentifiableEntity, B extends IdentifiableEntityBuilder<C, B>> {

        private Long id;

        public IdentifiableEntityBuilder() {
        }

        protected abstract B self();

        public abstract C build();

        public B id(Long id) {
            this.id = id;
            return this.self();
        }

        public String toString() {
            return "IdentifiableEntity.IdentifiableEntityBuilder(id=" + this.id + ")";
        }
    }
}
