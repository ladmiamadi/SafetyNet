package com.openclassrooms.safetynet.repository;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.openclassrooms.safetynet.model.Person;
import org.springframework.http.converter.json.MappingJacksonValue;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import static java.lang.Integer.parseInt;

public class HelperRepository {
    public static long calculateAge (String birthDate) {
        String[] str = birthDate.split("/");
        LocalDate start = LocalDate.of(parseInt(str[2]), parseInt(str[0]), parseInt(str[1]));
        LocalDate end = LocalDate.now();

        return ChronoUnit.YEARS.between(start, end);
    }

    public static MappingJacksonValue getFilter (String filterId, List listValues , String ... propertyArray) {
        SimpleBeanPropertyFilter alertFilter = SimpleBeanPropertyFilter.serializeAllExcept(propertyArray);
        FilterProvider myFilter = new SimpleFilterProvider().addFilter(filterId, alertFilter);
        MappingJacksonValue filteredList = new MappingJacksonValue(listValues);
        filteredList.setFilters(myFilter);
        return filteredList;
    }

    public static MappingJacksonValue getNoFilter(String filterId, Person person) {
        SimpleBeanPropertyFilter alertFilter = SimpleBeanPropertyFilter.serializeAll();
        FilterProvider myFilter = new SimpleFilterProvider().addFilter(filterId, alertFilter);
        MappingJacksonValue filteredList = new MappingJacksonValue(person);
        filteredList.setFilters(myFilter);
        return filteredList;
    }

    public static Map<String, Integer> countChildrenAndAdults(List<Person> personList) {
        Map<String, Integer> count = new LinkedHashMap<>();
        int countAdults=0;
        int countChildren=0;

        for(Person person: personList) {
            if(calculateAge(person.getMedicalRecords().getBirthDate()) <= 18) {
                countChildren = countChildren + 1;
            } else {
                countAdults = countAdults + 1;
            }
        }
        count.put("Number of children", countChildren);
        count.put("Number of adults", countAdults);

        return count;
    }
}
