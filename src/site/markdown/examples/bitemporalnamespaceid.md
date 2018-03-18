# Example: BiTemporalNamespaceId

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