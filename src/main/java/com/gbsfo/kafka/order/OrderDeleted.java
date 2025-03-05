/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package com.gbsfo.kafka.order;

import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.SchemaStore;
import org.apache.avro.specific.SpecificData;

@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class OrderDeleted extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = -7920949581317950197L;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"OrderDeleted\",\"namespace\":\"com.gbsfo.kafka.order\",\"fields\":[{\"name\":\"order_id\",\"type\":\"long\",\"doc\":\"Order's ID\"},{\"name\":\"number\",\"type\":\"string\",\"doc\":\"Number\"},{\"name\":\"orderStatus\",\"type\":{\"type\":\"enum\",\"name\":\"OrderStatus\",\"symbols\":[\"CREATED\",\"PROCESSING\",\"SHIPPING\",\"DELIVERED\"]}}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<OrderDeleted> ENCODER =
      new BinaryMessageEncoder<OrderDeleted>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<OrderDeleted> DECODER =
      new BinaryMessageDecoder<OrderDeleted>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   */
  public static BinaryMessageDecoder<OrderDeleted> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   */
  public static BinaryMessageDecoder<OrderDeleted> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<OrderDeleted>(MODEL$, SCHEMA$, resolver);
  }

  /** Serializes this OrderDeleted to a ByteBuffer. */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /** Deserializes a OrderDeleted from a ByteBuffer. */
  public static OrderDeleted fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  /** Order's ID */
  @Deprecated public long order_id;
  /** Number */
  @Deprecated public java.lang.CharSequence number;
  @Deprecated public com.gbsfo.kafka.order.OrderStatus orderStatus;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public OrderDeleted() {}

  /**
   * All-args constructor.
   * @param order_id Order's ID
   * @param number Number
   * @param orderStatus The new value for orderStatus
   */
  public OrderDeleted(java.lang.Long order_id, java.lang.CharSequence number, com.gbsfo.kafka.order.OrderStatus orderStatus) {
    this.order_id = order_id;
    this.number = number;
    this.orderStatus = orderStatus;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return order_id;
    case 1: return number;
    case 2: return orderStatus;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: order_id = (java.lang.Long)value$; break;
    case 1: number = (java.lang.CharSequence)value$; break;
    case 2: orderStatus = (com.gbsfo.kafka.order.OrderStatus)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'order_id' field.
   * @return Order's ID
   */
  public java.lang.Long getOrderId() {
    return order_id;
  }

  /**
   * Sets the value of the 'order_id' field.
   * Order's ID
   * @param value the value to set.
   */
  public void setOrderId(java.lang.Long value) {
    this.order_id = value;
  }

  /**
   * Gets the value of the 'number' field.
   * @return Number
   */
  public java.lang.CharSequence getNumber() {
    return number;
  }

  /**
   * Sets the value of the 'number' field.
   * Number
   * @param value the value to set.
   */
  public void setNumber(java.lang.CharSequence value) {
    this.number = value;
  }

  /**
   * Gets the value of the 'orderStatus' field.
   * @return The value of the 'orderStatus' field.
   */
  public com.gbsfo.kafka.order.OrderStatus getOrderStatus() {
    return orderStatus;
  }

  /**
   * Sets the value of the 'orderStatus' field.
   * @param value the value to set.
   */
  public void setOrderStatus(com.gbsfo.kafka.order.OrderStatus value) {
    this.orderStatus = value;
  }

  /**
   * Creates a new OrderDeleted RecordBuilder.
   * @return A new OrderDeleted RecordBuilder
   */
  public static com.gbsfo.kafka.order.OrderDeleted.Builder newBuilder() {
    return new com.gbsfo.kafka.order.OrderDeleted.Builder();
  }

  /**
   * Creates a new OrderDeleted RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new OrderDeleted RecordBuilder
   */
  public static com.gbsfo.kafka.order.OrderDeleted.Builder newBuilder(com.gbsfo.kafka.order.OrderDeleted.Builder other) {
    return new com.gbsfo.kafka.order.OrderDeleted.Builder(other);
  }

  /**
   * Creates a new OrderDeleted RecordBuilder by copying an existing OrderDeleted instance.
   * @param other The existing instance to copy.
   * @return A new OrderDeleted RecordBuilder
   */
  public static com.gbsfo.kafka.order.OrderDeleted.Builder newBuilder(com.gbsfo.kafka.order.OrderDeleted other) {
    return new com.gbsfo.kafka.order.OrderDeleted.Builder(other);
  }

  /**
   * RecordBuilder for OrderDeleted instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<OrderDeleted>
    implements org.apache.avro.data.RecordBuilder<OrderDeleted> {

    /** Order's ID */
    private long order_id;
    /** Number */
    private java.lang.CharSequence number;
    private com.gbsfo.kafka.order.OrderStatus orderStatus;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(com.gbsfo.kafka.order.OrderDeleted.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.order_id)) {
        this.order_id = data().deepCopy(fields()[0].schema(), other.order_id);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.number)) {
        this.number = data().deepCopy(fields()[1].schema(), other.number);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.orderStatus)) {
        this.orderStatus = data().deepCopy(fields()[2].schema(), other.orderStatus);
        fieldSetFlags()[2] = true;
      }
    }

    /**
     * Creates a Builder by copying an existing OrderDeleted instance
     * @param other The existing instance to copy.
     */
    private Builder(com.gbsfo.kafka.order.OrderDeleted other) {
            super(SCHEMA$);
      if (isValidValue(fields()[0], other.order_id)) {
        this.order_id = data().deepCopy(fields()[0].schema(), other.order_id);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.number)) {
        this.number = data().deepCopy(fields()[1].schema(), other.number);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.orderStatus)) {
        this.orderStatus = data().deepCopy(fields()[2].schema(), other.orderStatus);
        fieldSetFlags()[2] = true;
      }
    }

    /**
      * Gets the value of the 'order_id' field.
      * Order's ID
      * @return The value.
      */
    public java.lang.Long getOrderId() {
      return order_id;
    }

    /**
      * Sets the value of the 'order_id' field.
      * Order's ID
      * @param value The value of 'order_id'.
      * @return This builder.
      */
    public com.gbsfo.kafka.order.OrderDeleted.Builder setOrderId(long value) {
      validate(fields()[0], value);
      this.order_id = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'order_id' field has been set.
      * Order's ID
      * @return True if the 'order_id' field has been set, false otherwise.
      */
    public boolean hasOrderId() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'order_id' field.
      * Order's ID
      * @return This builder.
      */
    public com.gbsfo.kafka.order.OrderDeleted.Builder clearOrderId() {
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'number' field.
      * Number
      * @return The value.
      */
    public java.lang.CharSequence getNumber() {
      return number;
    }

    /**
      * Sets the value of the 'number' field.
      * Number
      * @param value The value of 'number'.
      * @return This builder.
      */
    public com.gbsfo.kafka.order.OrderDeleted.Builder setNumber(java.lang.CharSequence value) {
      validate(fields()[1], value);
      this.number = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'number' field has been set.
      * Number
      * @return True if the 'number' field has been set, false otherwise.
      */
    public boolean hasNumber() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'number' field.
      * Number
      * @return This builder.
      */
    public com.gbsfo.kafka.order.OrderDeleted.Builder clearNumber() {
      number = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'orderStatus' field.
      * @return The value.
      */
    public com.gbsfo.kafka.order.OrderStatus getOrderStatus() {
      return orderStatus;
    }

    /**
      * Sets the value of the 'orderStatus' field.
      * @param value The value of 'orderStatus'.
      * @return This builder.
      */
    public com.gbsfo.kafka.order.OrderDeleted.Builder setOrderStatus(com.gbsfo.kafka.order.OrderStatus value) {
      validate(fields()[2], value);
      this.orderStatus = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'orderStatus' field has been set.
      * @return True if the 'orderStatus' field has been set, false otherwise.
      */
    public boolean hasOrderStatus() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'orderStatus' field.
      * @return This builder.
      */
    public com.gbsfo.kafka.order.OrderDeleted.Builder clearOrderStatus() {
      orderStatus = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public OrderDeleted build() {
      try {
        OrderDeleted record = new OrderDeleted();
        record.order_id = fieldSetFlags()[0] ? this.order_id : (java.lang.Long) defaultValue(fields()[0]);
        record.number = fieldSetFlags()[1] ? this.number : (java.lang.CharSequence) defaultValue(fields()[1]);
        record.orderStatus = fieldSetFlags()[2] ? this.orderStatus : (com.gbsfo.kafka.order.OrderStatus) defaultValue(fields()[2]);
        return record;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<OrderDeleted>
    WRITER$ = (org.apache.avro.io.DatumWriter<OrderDeleted>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<OrderDeleted>
    READER$ = (org.apache.avro.io.DatumReader<OrderDeleted>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

}
