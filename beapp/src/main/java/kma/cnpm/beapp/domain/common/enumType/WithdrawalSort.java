package kma.cnpm.beapp.domain.common.enumType;

public enum WithdrawalSort {
        CREATE_ASC("createdAt", "asc"),
    CREATE_DESC("createdAt", "desc"),
    AMOUNT_ASC("amount", "asc"),
    AMOUNT_DESC("amount", "desc");

    private final String field;
    private final String direction;

    WithdrawalSort(String field, String direction) {
        this.field = field;
        this.direction = direction;
    }

    public String getField() {
        return field;
    }

    public String getDirection() {
        return direction;
    }

//    public static WithdrawalSort fromString(String sortBy) {
//        for (WithdrawalSort sort : WithdrawalSort.values()) {
//            if (sort.name().equalsIgnoreCase(sortBy)) {
//                return sort;
//            }
//        }
//        throw new IllegalArgumentException("Invalid sort value: " + sortBy);
//    }
}
