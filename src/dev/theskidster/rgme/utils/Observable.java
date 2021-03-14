package dev.theskidster.rgme.utils;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;

/**
 * @author J Hoffman
 * Created: Mar 13, 2021
 */

public class Observable {
    
    private final PropertyChangeSupport observable;
    public Map<String, Object> properties = new HashMap<>();
    
    public Observable(Object object) {
        observable = new PropertyChangeSupport(object);
    }
    
    public void addObserver(PropertyChangeListener observer) {
        observable.addPropertyChangeListener(observer);
    }
    
    public void removeObserver(PropertyChangeListener observer) {
        observable.removePropertyChangeListener(observer);
    }
    
    public void notifyObservers(String name, Object property) {
        observable.firePropertyChange(name, properties.get(name), property);
        properties.put(name, property);
    }
    
}