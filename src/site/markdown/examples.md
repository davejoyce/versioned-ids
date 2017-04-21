# Examples

Here are a few examples of how to use the different ID types in this library.

## Using NamespaceId

The `NamespaceId` type is the root type of the Versioned IDs library. Use it
when you need namespace support for categorical organization of your objects,
but you don't have any time sensitivity around your object version.

```java
public class Employee implements Comparable<Employee> {

    private final NamespaceId<Integer> employeeId;
    private final String firstName;
    private final String lastName;

    public Employee(int employeeNumber, String firstName, String lastName, String team) {
        this.employeeId = new NamespaceId<>(team, employeeNumber);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // getters...
    
    // Natural sort by team, employee#
    public int compareTo(Employee other) {
        if (this == other) return 0;
        return this.employeeId.compareTo(other.employeeId);
    }

    public String toString() {
        return String.format("%1s %2s %3s", employeeId, firstName, lastName);
    }
    
    // equals & hashCode...
}

SortedSet<Employee> employees = new HashSet<>();
employees.add(new Employee(1, "Smith", "John", "development"));
employees.add(new Employee(2, "Kumar", "Ram", "accounting"));
employees.add(new Employee(3, "Lopez", "Marcos", "operations"));

employees.stream().forEach(e -> System.out.println(e));
// Prints
// accounting/2 Ram Kumar
// development/1 John Smith
// operations/3 Marcos Lopez
```

## Using TemporalNamespaceId

The `TemporalNamespaceId` class extends `NamespaceId` to add the notion of an
effective or _"as of"_ time. It is well suited for scenarios where the domain
model class concerned has a moment in time from which a particular version is
in effect.

```java
public class Definition {

    private final TemporalNamespaceId<Integer> definitionId;
    private final String name;

    public Definition(int sequenceNumber, String name, String category) {
        this.definitionId = new TemporalNamespaceId<>(category, sequenceNumber, Instant.now());
        this.name = name;
    }
    
    public TemporalNamespaceId<Integer> getDefinitionId() {
        return definitionId;
    }
    
    public String toString() {
        return name;
    }
}

// The definition of what a digital calculator is changes over time...
Definition v1 = new Definition(1, "pocket calculator", "digital calculator");
Definition v2 = new Definition(1, "personal computer", "digital calculator");
Definition v3 = new Definition(1, "smartphone", "digital calculator");

SortedMap<TemporalNamespaceId<Integer>, Definition> definitionMap = new TreeMap<>();
definitionMap.put(v1.getDefinitionId(), v1);
definitionMap.put(v2.getDefinitionId(), v2);
definitionMap.put(v3.getDefinitionId(), v3);

definitionMap.forEach((id, val) -> System.out.println(id.getAsOfTime() + " - " + val));
// Prints
// 2017-04-08T09:36:28.108533Z - pocket calculator
// 2017-04-08T09:36:29.079624Z - personal computer
// 2017-04-08T09:36:29.218305Z - smartphone
```

## Using BiTemporalNamespaceId

The `BiTemporalNamespaceId` class extends `TemporalNamespaceId` to add the
notion of an observed or _"as at"_ time. It is well suited for scenarios where
the domain model class concerned has a moment at or beyond the _"as of"_ time
when the value is fixed.

```java
public class StockPrice {
    
    private final BiTemporalNamespaceId<String> stockPriceId;
    private final String exchange;
    private final String symbol;
    private final double quotedPrice;
    private final double executedPrice;

    public StockPrice(String exchange,
                      String symbol,
                      double quotedPrice,
                      Instant quoteTimestamp,
                      double executedPrice,
                      Instant executionTimestamp) {
        this.exchange = exchange;
        this.symbol = symbol;
        this.quotedPrice = quotedPrice;
        this.executedPrice = executedPrice;
        this.stockPriceId = new BiTemporalNamespaceId<>(exchange, symbol, quoteTimestamp, executionTimestamp);
    }
    
    public BiTemporalNamespaceId<String> getStockPriceId() {
        return stockPriceId;
    }
    
    public String toString() {
        return symbol + " (" + exchange + ") " + quotedPrice + "/" + executedPrice;
    }
}
```