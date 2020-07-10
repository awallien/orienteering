package util.point;

import java.util.Objects;

/**
 * A helper class data structure for BFS algorithm
 *
 * @param <E>
 * @param <T>
 */
public class State<E,T> {

    private E data;
    private T value;

    public State(E data, T value) {
        this.data = data;
        this.value = value;
    }

    public E getData() {
        return data;
    }

    public T getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State<?, ?> state = (State<?, ?>) o;
        return Objects.equals(data, state.data) &&
                Objects.equals(value, state.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, value);
    }

    @Override
    public String toString() {
        return "State{" +
                "data=" + data +
                ", value=" + value +
                '}';
    }
}
