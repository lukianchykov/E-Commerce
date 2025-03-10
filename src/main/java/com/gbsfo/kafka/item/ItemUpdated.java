/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package com.gbsfo.kafka.item;

import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.SchemaStore;
import org.apache.avro.specific.SpecificData;

@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class ItemUpdated extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = 6456618747126511357L;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"ItemUpdated\",\"namespace\":\"com.gbsfo.kafka.item\",\"fields\":[{\"name\":\"id\",\"type\":\"long\",\"doc\":\"Item's ID\"},{\"name\":\"name\",\"type\":\"string\",\"doc\":\"Name\"},{\"name\":\"price\",\"type\":{\"type\":\"bytes\",\"logicalType\":\"decimal\",\"precision\":10,\"scale\":2}}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<ItemUpdated> ENCODER =
      new BinaryMessageEncoder<ItemUpdated>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<ItemUpdated> DECODER =
      new BinaryMessageDecoder<ItemUpdated>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   */
  public static BinaryMessageDecoder<ItemUpdated> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   */
  public static BinaryMessageDecoder<ItemUpdated> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<ItemUpdated>(MODEL$, SCHEMA$, resolver);
  }

  /** Serializes this ItemUpdated to a ByteBuffer. */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /** Deserializes a ItemUpdated from a ByteBuffer. */
  public static ItemUpdated fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  /** Item's ID */
  @Deprecated public long id;
  /** Name */
  @Deprecated public java.lang.CharSequence name;
  @Deprecated public java.nio.ByteBuffer price;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public ItemUpdated() {}

  /**
   * All-args constructor.
   * @param id Item's ID
   * @param name Name
   * @param price The new value for price
   */
  public ItemUpdated(java.lang.Long id, java.lang.CharSequence name, java.nio.ByteBuffer price) {
    this.id = id;
    this.name = name;
    this.price = price;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return id;
    case 1: return name;
    case 2: return price;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  protected static final org.apache.avro.data.TimeConversions.DateConversion DATE_CONVERSION = new org.apache.avro.data.TimeConversions.DateConversion();
  protected static final org.apache.avro.data.TimeConversions.TimeConversion TIME_CONVERSION = new org.apache.avro.data.TimeConversions.TimeConversion();
  protected static final org.apache.avro.data.TimeConversions.TimestampConversion TIMESTAMP_CONVERSION = new org.apache.avro.data.TimeConversions.TimestampConversion();
  protected static final org.apache.avro.Conversions.DecimalConversion DECIMAL_CONVERSION = new org.apache.avro.Conversions.DecimalConversion();

  private static final org.apache.avro.Conversion<?>[] conversions =
      new org.apache.avro.Conversion<?>[] {
      null,
      null,
      null,
      null
  };

  @Override
  public org.apache.avro.Conversion<?> getConversion(int field) {
    return conversions[field];
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: id = (java.lang.Long)value$; break;
    case 1: name = (java.lang.CharSequence)value$; break;
    case 2: price = (java.nio.ByteBuffer)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'id' field.
   * @return Item's ID
   */
  public java.lang.Long getId() {
    return id;
  }

  /**
   * Sets the value of the 'id' field.
   * Item's ID
   * @param value the value to set.
   */
  public void setId(java.lang.Long value) {
    this.id = value;
  }

  /**
   * Gets the value of the 'name' field.
   * @return Name
   */
  public java.lang.CharSequence getName() {
    return name;
  }

  /**
   * Sets the value of the 'name' field.
   * Name
   * @param value the value to set.
   */
  public void setName(java.lang.CharSequence value) {
    this.name = value;
  }

  /**
   * Gets the value of the 'price' field.
   * @return The value of the 'price' field.
   */
  public java.nio.ByteBuffer getPrice() {
    return price;
  }

  /**
   * Sets the value of the 'price' field.
   * @param value the value to set.
   */
  public void setPrice(java.nio.ByteBuffer value) {
    this.price = value;
  }

  /**
   * Creates a new ItemUpdated RecordBuilder.
   * @return A new ItemUpdated RecordBuilder
   */
  public static com.gbsfo.kafka.item.ItemUpdated.Builder newBuilder() {
    return new com.gbsfo.kafka.item.ItemUpdated.Builder();
  }

  /**
   * Creates a new ItemUpdated RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new ItemUpdated RecordBuilder
   */
  public static com.gbsfo.kafka.item.ItemUpdated.Builder newBuilder(com.gbsfo.kafka.item.ItemUpdated.Builder other) {
    return new com.gbsfo.kafka.item.ItemUpdated.Builder(other);
  }

  /**
   * Creates a new ItemUpdated RecordBuilder by copying an existing ItemUpdated instance.
   * @param other The existing instance to copy.
   * @return A new ItemUpdated RecordBuilder
   */
  public static com.gbsfo.kafka.item.ItemUpdated.Builder newBuilder(com.gbsfo.kafka.item.ItemUpdated other) {
    return new com.gbsfo.kafka.item.ItemUpdated.Builder(other);
  }

  /**
   * RecordBuilder for ItemUpdated instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<ItemUpdated>
    implements org.apache.avro.data.RecordBuilder<ItemUpdated> {

    /** Item's ID */
    private long id;
    /** Name */
    private java.lang.CharSequence name;
    private java.nio.ByteBuffer price;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(com.gbsfo.kafka.item.ItemUpdated.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.id)) {
        this.id = data().deepCopy(fields()[0].schema(), other.id);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.name)) {
        this.name = data().deepCopy(fields()[1].schema(), other.name);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.price)) {
        this.price = data().deepCopy(fields()[2].schema(), other.price);
        fieldSetFlags()[2] = true;
      }
    }

    /**
     * Creates a Builder by copying an existing ItemUpdated instance
     * @param other The existing instance to copy.
     */
    private Builder(com.gbsfo.kafka.item.ItemUpdated other) {
            super(SCHEMA$);
      if (isValidValue(fields()[0], other.id)) {
        this.id = data().deepCopy(fields()[0].schema(), other.id);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.name)) {
        this.name = data().deepCopy(fields()[1].schema(), other.name);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.price)) {
        this.price = data().deepCopy(fields()[2].schema(), other.price);
        fieldSetFlags()[2] = true;
      }
    }

    /**
      * Gets the value of the 'id' field.
      * Item's ID
      * @return The value.
      */
    public java.lang.Long getId() {
      return id;
    }

    /**
      * Sets the value of the 'id' field.
      * Item's ID
      * @param value The value of 'id'.
      * @return This builder.
      */
    public com.gbsfo.kafka.item.ItemUpdated.Builder setId(long value) {
      validate(fields()[0], value);
      this.id = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'id' field has been set.
      * Item's ID
      * @return True if the 'id' field has been set, false otherwise.
      */
    public boolean hasId() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'id' field.
      * Item's ID
      * @return This builder.
      */
    public com.gbsfo.kafka.item.ItemUpdated.Builder clearId() {
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'name' field.
      * Name
      * @return The value.
      */
    public java.lang.CharSequence getName() {
      return name;
    }

    /**
      * Sets the value of the 'name' field.
      * Name
      * @param value The value of 'name'.
      * @return This builder.
      */
    public com.gbsfo.kafka.item.ItemUpdated.Builder setName(java.lang.CharSequence value) {
      validate(fields()[1], value);
      this.name = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'name' field has been set.
      * Name
      * @return True if the 'name' field has been set, false otherwise.
      */
    public boolean hasName() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'name' field.
      * Name
      * @return This builder.
      */
    public com.gbsfo.kafka.item.ItemUpdated.Builder clearName() {
      name = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'price' field.
      * @return The value.
      */
    public java.nio.ByteBuffer getPrice() {
      return price;
    }

    /**
      * Sets the value of the 'price' field.
      * @param value The value of 'price'.
      * @return This builder.
      */
    public com.gbsfo.kafka.item.ItemUpdated.Builder setPrice(java.nio.ByteBuffer value) {
      validate(fields()[2], value);
      this.price = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'price' field has been set.
      * @return True if the 'price' field has been set, false otherwise.
      */
    public boolean hasPrice() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'price' field.
      * @return This builder.
      */
    public com.gbsfo.kafka.item.ItemUpdated.Builder clearPrice() {
      price = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ItemUpdated build() {
      try {
        ItemUpdated record = new ItemUpdated();
        record.id = fieldSetFlags()[0] ? this.id : (java.lang.Long) defaultValue(fields()[0], record.getConversion(0));
        record.name = fieldSetFlags()[1] ? this.name : (java.lang.CharSequence) defaultValue(fields()[1], record.getConversion(1));
        record.price = fieldSetFlags()[2] ? this.price : (java.nio.ByteBuffer) defaultValue(fields()[2], record.getConversion(2));
        return record;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<ItemUpdated>
    WRITER$ = (org.apache.avro.io.DatumWriter<ItemUpdated>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<ItemUpdated>
    READER$ = (org.apache.avro.io.DatumReader<ItemUpdated>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

}
