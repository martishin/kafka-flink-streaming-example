package main

import (
	"context"
	"encoding/json"
	"fmt"
	"log"
	"time"

	"github.com/confluentinc/confluent-kafka-go/v2/kafka"
	"github.com/jackc/pgx/v5"
)

type Order struct {
	CustomerID int
	Category   string
	Cost       float64
	ItemName   string
}

var (
	databaseURL = "postgres://postgres:postgres@postgres:5432/postgres"
	kafkaNodes  = "kafka:9092"
	myTopic     = "order"
)

func genData(order Order, producer *kafka.Producer) {
	myData := map[string]interface{}{
		"category": order.Category,
		"cost":     order.Cost,
	}

	jsonValue, err := json.Marshal(myData)
	if err != nil {
		log.Fatalf("Failed to marshal JSON: %v", err)
	}

	msg := &kafka.Message{
		TopicPartition: kafka.TopicPartition{Topic: &myTopic, Partition: kafka.PartitionAny},
		Value:          jsonValue,
	}

	err = producer.Produce(msg, nil)
	if err != nil {
		log.Fatalf("Failed to produce message: %v", err)
	}

	// Wait for message deliveries
	producer.Flush(15 * 1000)

	fmt.Printf("Sent: %d %s %f %s\n", order.CustomerID, order.Category, order.Cost, order.ItemName)
}

func main() {
	time.Sleep(20 * time.Second)

	// Connect to PostgreSQL database using pgx
	conn, err := pgx.Connect(context.Background(), databaseURL)
	if err != nil {
		log.Fatalf("Unable to connect to database: %v", err)
	}
	defer conn.Close(context.Background())

	// Create a new Kafka producer using confluent-kafka-go
	producer, err := kafka.NewProducer(&kafka.ConfigMap{"bootstrap.servers": kafkaNodes})
	if err != nil {
		log.Fatalf("Failed to create Kafka producer: %v", err)
	}
	defer producer.Close()

	// Query the orders table
	rows, err := conn.Query(context.Background(), "SELECT customer_id, category, cost, item_name FROM orders")
	if err != nil {
		log.Fatalf("Failed to query database: %v", err)
	}
	defer rows.Close()

	// Process each row and send data to Kafka
	for rows.Next() {
		var order Order
		err := rows.Scan(&order.CustomerID, &order.Category, &order.Cost, &order.ItemName)
		if err != nil {
			log.Fatalf("Failed to scan row: %v", err)
		}
		genData(order, producer)
	}

	if err := rows.Err(); err != nil {
		log.Fatalf("Error iterating rows: %v", err)
	}
}
