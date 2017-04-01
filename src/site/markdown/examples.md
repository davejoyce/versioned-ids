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

    getters...
    
    // Natural sort by team, employee#
    public int compareTo(Employee other) {
        if (this == other) return 0;
        return this.employeeId.compareTo(other.employeeId);
    }

    public String toString() {
        return String.format("%1s %2s, %3s", employeeId, firstName, lastName);
    }
    
    equals & hashCode...
}

```