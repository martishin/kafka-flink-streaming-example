-- Create the 'orders' table
CREATE TABLE orders (
  id INT,
  customer_id INT,
  category VARCHAR(255),
  cost DOUBLE PRECISION,
  item_name VARCHAR(255)
);