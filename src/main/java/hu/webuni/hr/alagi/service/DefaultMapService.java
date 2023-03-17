package hu.webuni.hr.alagi.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
@Primary
public class DefaultMapService implements MapService<Long> {

   @Override
   public Long getFirstFreeKey(Set<Long> keySet) {
      if (keySet==null) {
         throw new IllegalArgumentException("The given keySet is null");
      }
      if (keySet.isEmpty()) {
         return 1L;
      }
      List<Long> keyList = new ArrayList<>(keySet.stream().toList());
      Collections.sort(keyList);
      if (keyList.get(0)>1) {
         return 1L;
      }
      if (keyList.size()==1) {
         return keyList.get(0)+1;
      }
      for (int i = 0;i<keyList.size()-1;i++) {
         if (keyList.get(i+1)-keyList.get(i)>1) {
            return keyList.get(i)+1;
         }
      }
      return (long) keyList.size()+1;
   }
}
