import java.util.Objects;

public class Order {

  /*
  {
    "category": "Category 1",
    "cost": "10.34"
  }
  */

  public String category;
  public Double cost;

  public Order() {}

  public Order(String category, String cost) {
    this.category = category;
    this.cost = Double.valueOf(cost);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Order{");
    sb.append("category=").append(category).append('\'');
    sb.append(", cost=").append(String.valueOf(cost)).append('\'');
    return sb.toString();
  }

  public int hashCode() {
    return Objects.hash(super.hashCode(), category, cost);
  }
}