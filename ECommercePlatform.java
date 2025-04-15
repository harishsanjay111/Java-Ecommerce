
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;



public  class Product {
    private String name;
    private int price;
    private int salesCount; 
    public Product(String name,int price){
    this.name = name;
    this.price = price;
    this.salesCount = 0;
    }
public  String getName(){
    return name;
}
public int getPrice(){
    return price;
}
public int getSalesCount(){
    return salesCount;
}
public void incrementSalesCount(){
    salesCount++;
}

 }

 class User {
private  String userId;
private List<Product>  purchasedProducts;

public User(String userId){
    this.userId = userId;
    this.purchasedProducts = new ArrayList<>();
}
public void purchaseProducts(Product product){
    purchasedProducts.add(product);
    product.incrementSalesCount();

}
public List<Product> getpurchaseProducts(){
    return purchasedProducts;
}
 }

 class Trienode {
    Map<Character,Trienode> children = new HashMap<>();
    boolean isEndofWord;
 }


 class ProductService {
    private Trienode root;
    public ProductService() {
        root = new Trienode();
    }

    public void insert(String productName) {
   Trienode current = root;
   for(char ch : productName.toCharArray()){
       current = current.children.computeIfAbsent(ch, c -> new Trienode());
   }
   current.isEndofWord = true;


    }

    public List<String> searchPrefix(String prefix) {
             List<String> results = new ArrayList<>();
            
        Trienode node = findNode(prefix);

if(node != null) {
    dfs( node  , new StringBuffer(prefix),results);
}

      return results;
    }

    private Trienode findNode(String prefix){
           Trienode current = root;
       for(char ch : prefix.toCharArray()){
           current = current.children.get(ch);
           if(current == null) return  null;
        }
        return current;
        

    }
    private void   dfs(Trienode node ,  StringBuffer current , List<String> results){
     
        if(node.isEndofWord) {
            results.add(current.toString());
        }
        for(char ch : node.children.keySet()){
            current.append(ch);
             dfs(node.children.get(ch), current, results);
            current.deleteCharAt(current.length() - 1); // here iam using backtrack
        }


    }

 }


 //this is the RecommendationEngine using BFS
class RecommendationEngine {
    private Map<User,List<Product>>  userproductGraph;
    public RecommendationEngine () {
        userproductGraph = new HashMap<>();
    }

    public void addPurchase(User user, Product product) {
        userproductGraph.computeIfAbsent(user, k -> new ArrayList<>()).add(product);

    }
    public List<Product> getRecommendations (User user) {
        List<Product> recommendations = new ArrayList<>();
        Queue<Product> queue = new LinkedList<>();
        Set<Product> visitedproducts = new HashSet<>();
          
        List<Product> userProducts = userproductGraph.getOrDefault(user, new ArrayList<>()); 

        for(Product product : userProducts){
            queue.add(product);
            visitedproducts.add(product);
        while(!queue.isEmpty()){
            Product current = queue.poll();

            for(User otherUser : userproductGraph.keySet()){
                if(otherUser == user) continue;
              List<Product> otherproducts = userproductGraph.getOrDefault(otherUser, new ArrayList<>());
              if(otherproducts.contains(current)){
                for(Product p : otherproducts){
                    if(!visitedproducts.contains(p) && !userProducts.contains(p)){
                        recommendations.add(p);
                        visitedproducts.add(p);
                        queue.add(p);           
                    }
                }

              }
 
            }
        }
        }


          return recommendations;
    }


   
}
class InventoryManager {
    private PriorityQueue<Product> restockQueue;

   

    public void addToRestockQueue(Product product) {
        restockQueue.add(product);
    }

    public InventoryManager() {
        restockQueue = new PriorityQueue<>(
            (a, b) -> b.getSalesCount() - a.getSalesCount() 
        );
    }

    public Product getNextRestockProduct() {
        return restockQueue.poll();
    }
}


public class ECommercePlatform {
public static void main(String[] args) {
    //products obj
  Product laptop = new Product("laptop", 80000);
  Product lamp = new Product("lamp", 29000);
  Product camera = new Product("camera", 45000);
//user obj
  User ramesh = new User("ramesh");
  User suresh = new User("suresh");

  ramesh.purchaseProducts(laptop);
ramesh.purchaseProducts(camera);
suresh.purchaseProducts(lamp);

  ProductService search = new ProductService();
        search.insert("laptop");
        search.insert("lamp");
        search.insert("camera");
        //autocomplete implementation
System.out.println("Autocomplete suggestions for 'la': " + search.searchPrefix("la"));



RecommendationEngine engine = new RecommendationEngine();

    engine.addPurchase(ramesh, laptop);
    engine.addPurchase(ramesh, camera);
    engine.addPurchase(suresh, laptop);

    System.out.println("the Reccomendation for ramesh : " + engine.getRecommendations(suresh).stream()
    .map(Product::getName)
    .toList()
    );

    InventoryManager inventory = new InventoryManager();
    inventory.addToRestockQueue(laptop);
    inventory.addToRestockQueue(lamp);
    inventory.addToRestockQueue(camera);

    System.out.println("Next product to restock: " + 
    inventory.getNextRestockProduct().getName()
);
    }


}
