package io.makingthematrix.fiftystates.model;

import javafx.beans.value.ObservableValue;

import java.util.function.Consumer;

final public class ChangeListenerWrapper<T> implements javafx.beans.value.ChangeListener<T> {
    private final Consumer<T> onNewValue;

    public ChangeListenerWrapper(Consumer<T> onNewValue) {
        this.onNewValue = onNewValue;
    }

    @Override
    public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
        onNewValue.accept(newValue);
    }
}
