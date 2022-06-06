package kernel;

public enum Status {
    NONE,
    LOADED, // val 1
    OPTIMAL, // val 2
    INFEASIBLE,
    INF_OR_UNBD,
    UNBOUNDED,
    CUTOFF,
    ITERATION_LIMIT,
    NODE_LIMIT,
    TIME_LIMIT,
    SOLUTION_LIMIT, // val 10
    INTERRUPTED,
    NUMERIC,
    SUBOPTIMAL,
    INPROGRESS,
    USER_OBJ_LIMIT
}
