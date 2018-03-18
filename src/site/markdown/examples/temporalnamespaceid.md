# Example: TemporalNamespaceId

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
```

```java
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