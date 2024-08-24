from sqlalchemy import create_engine, Column, Integer, String, Float, MetaData, Table
from sqlalchemy.orm import declarative_base, Session
from sqlalchemy.sql import func
from random import randint
import time

# Replace 'your_postgres_user', 'your_postgres_password', 'your_postgres_database', and 'your_postgres_host' with your actual PostgreSQL credentials
DATABASE_URL = "postgresql+psycopg2://postgres:postgres@postgres:5432/postgres"

try:
    time.sleep(5)
    # SQLAlchemy engine
    engine = create_engine(DATABASE_URL)

    # Declare a base
    Base = declarative_base()

    # Create the table (if not exists)
    Base.metadata.create_all(engine)

    # Create a session
    session = Session(engine)

    # Define the 'orders' table
    class Order(Base):
        __tablename__ = "orders"
        customer_id = Column(Integer, primary_key=True)
        category = Column(String(255))
        cost = Column(Float)
        item_name = Column(String(255))

    # Insert 100,000 rows of dummy data
    for i in range(1, 100001):
        order = Order(
            customer_id=i,
            category=f"Category {randint(1, 10)}",
            cost=i * 10.5,
            item_name=f"Item {i}"
        )
        session.add(order)

    # Commit the transaction
    session.commit()

    # Query and print the first 5 rows
    orders = session.query(Order).limit(5).all()
    for order in orders:
        print(order.customer_id, order.category, order.cost, order.item_name)

    # Close the session
    session.close()
except Exception as e:
    print(e)
