import org.apache.spark.sql._

object BikeData extends App {

  import org.apache.log4j.{Level, Logger}
  Logger.getLogger("org").setLevel(Level.OFF)

  val spark = SparkSession.builder()
    .master( master= "local[*]")
    .appName( name= "Bike Data")
    .getOrCreate()

  val df_cust = spark.read
    .option("header", "true")
    .option("inferSchema", "true")
    .csv("data/customer.csv")
  df_cust.printSchema()
  df_cust.show(false)

  val df_transaction = spark.read
    .option("header", "true")
    .option("inferSchema", "true")
    .csv("data/transaction.csv")
  df_transaction.printSchema()
  df_transaction.show(false)

  val df_item = spark.read
    .option("header", "true")
    .option("inferSchema", "true")
    .csv("data/item.csv")
  df_item.printSchema()
  df_item.show(false)

  val df_payment = spark.read
    .option("header", "true")
    .option("inferSchema", "true")
    .csv("data/payment.csv")
  df_payment.printSchema()
  df_payment.show(false)

  val df_combined = df_transaction
    .join(df_cust, "cust_id")
    .join(df_item, "item_id")
    .join(df_payment,"payment_type")
  df_combined.show(false)

  val df_distinct_cust_item
    = df_combined.dropDuplicates("cust_id", "item_id")
  df_distinct_cust_item.show(false)

  val df_distinct_cust_payment
    = df_combined.dropDuplicates("cust_id", "payment_type")
  df_distinct_cust_payment.show(false)

  val gds_payment_age = df_distinct_cust_payment
    .groupBy("payment_type")
    .mean("age")
    .join(df_payment, "payment_type")
    .show(false)

  val gds_item_age = df_distinct_cust_item
    .groupBy("item_id")
    .mean("age")
    .join(df_item, "item_id")
    .show(false)

}
