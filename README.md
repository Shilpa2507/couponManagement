# couponManagement

Implemented Cases

1. Create Coupon API:
   Supports creating all three types of coupons (Cart Wise, Product Wise, Buy X Get Y) along with an expiration date.

2. Update Coupon API:
   
  Validates if the coupon ID exists:
     If the ID exists, updates the coupon details.
     If the ID does not exist, throws an exception.

4. Get All Coupons API:
   
   Retrieves a list of all available coupons.

6. Get Coupon by ID API:
   
   Validates if the coupon ID exists:
     If the ID exists, fetches the coupon details.
     If the ID does not exist, throws an exception.

8. Delete Coupon by ID API:
   
   Validates if the coupon ID exists:
    If the ID exists, deletes the coupon.
    If the ID does not exist, throws an exception.

10. Get All Applicable Coupons API:
    
   Fetches all applicable coupons for the provided cart:
     Considers only coupons that are not expired.
     Returns a message if no coupons are applicable.

12. Apply Coupon by ID API:
    
   Validates the following before applying the coupon:
    Checks if the coupon ID exists, otherwise throws an exception.
    Ensures the coupon is applicable for the given cart, else provides an appropriate message.
    Confirms the coupon is not expired, else provides an expiration message.
    If valid, applies the coupon and returns the updated cart with the applied discount.

14. Database
    
   The application uses H2 Database for storing and managing coupon data. H2 is used as an in-memory database during development for testing and quick prototyping.
   The database is configured to persist coupon information, expiration details and conditions.

Unimplemented Cases

1. Input Validation:
   
   Assumes all inputs provided to the APIs are valid.
   No validation logic has been implemented to handle invalid or malformed inputs.

3. Changing Coupon Type During Update
   
   The current implementation does not support changing the type of a coupon during an update operation.
   This decision was made to simplify the logic and ensure data consistency, as changing the coupon type could potentially invalidate associated data (e.g., thresholds, product lists).

5. Supporting Multiple Inputs in Cart Wise and Product Wise Coupons
   
   Currently, the code assumes that Cart Wise coupons have a single threshold and discount entry, and Product Wise coupons have a single product ID and corresponding discount.
   The feature to support multiple thresholds, discounts, or product-discount mappings as a list is not implemented.

Limitations

1. Single Threshold and Discount:

   For Cart Wise only one threshold and one discount entry are considered and for Product Wise only a single product ID and corresponding discount is considered. This can be improved by 
   supporting multiple entries.
   Buy X Get Y coupons support multiple entries. 

2. Input Assumptions:
   
   Assumes that all inputs are valid, including coupon details, cart details, and thresholds.

Assumptions

1. Valid Inputs:
   Assumes that all inputs (cart details, coupon data) provided to the APIs are valid and do not require additional validation.

2. Sample Input Reference:
   Implementation is based on the structure and cases provided in the sample inputs.

3. Coupon Type is Immutable
   The coupon type (e.g., Cart Wise, Product Wise, or Buy X Get Y) is immutable. Once a coupon is created, its type cannot be changed via the update API. Any attempt to modify the coupon 
   type will result in an error.


