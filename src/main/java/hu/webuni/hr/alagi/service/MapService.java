package hu.webuni.hr.alagi.service;

import java.util.Set;

public interface MapService<K extends Number> {
   Long getFirstFreeKey(Set<K> keySet);
}
