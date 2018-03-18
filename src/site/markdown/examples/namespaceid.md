# Example: NamespaceId

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
        return String.format("%1$s %2$s %3$s", employeeId, firstName, lastName);
    }
    
    // equals + hashCode...
}
```
```java
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