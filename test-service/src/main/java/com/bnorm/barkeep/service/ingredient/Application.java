package com.bnorm.barkeep.service.ingredient;

import com.bnorm.barkeep.data.ingredient.Ingredient;
import com.bnorm.barkeep.repo.ingredient.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//@EnableDiscoveryClient
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

//@RestController
//class ServiceInstanceRestController {
//
//    private final DiscoveryClient discoveryClient;
//
//    @Autowired
//    public ServiceInstanceRestController(DiscoveryClient discoveryClient) {
//        this.discoveryClient = discoveryClient;
//    }
//
//    @RequestMapping("/service-instances/{applicationName}")
//    public List<ServiceInstance> serviceInstancesByApplicationName(@PathVariable String applicationName) {
//        return this.discoveryClient.getInstances(applicationName);
//    }
//}

//@RestController
//class IngredientRestController {
//
//    private final IngredientRepository repository;
//
//    @Autowired
//    public IngredientRestController(IngredientRepository repository) {
//        this.repository = repository;
//    }
//
//    @RequestMapping("/ingredients")
//    public List<Ingredient> ingredients() {
//        int max = 10;
//        int i = 0;
//
//        List<Ingredient> ingredients = new ArrayList<>(max);
//        for (Iterator<Ingredient> iterator = repository.findAll().iterator(); iterator.hasNext() && i < max; i++) {
//            ingredients.add(iterator.next());
//        }
//        return ingredients;
//    }
//}