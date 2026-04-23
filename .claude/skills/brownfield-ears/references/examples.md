# EARS Conversion Examples

## Example 1: User Login

### Original Requirement
"Users can log into the system"

### EARS Conversion

| ID | Requirement |
|----|-------------|
| AUTH-EV-001 | When user enters correct username and password and clicks login button, the system shall verify credentials and redirect to homepage |
| AUTH-EV-002 | When user enters incorrect username or password and clicks login button, the system shall display error message and retain username input |
| AUTH-ST-001 | If user is already logged in, then the system shall redirect to homepage instead of login page |
| AUTH-CX-001 | When user attempts login, and has failed consecutively more than 5 times, the system shall lock account for 15 minutes and send security notification |
| AUTH-UB-001 | The system shall encrypt all passwords using bcrypt with cost factor 12 |

---

## Example 2: Shopping Cart

### Original Requirement
"Users should be able to add items to cart and checkout"

### EARS Conversion

| ID | Requirement |
|----|-------------|
| CART-EV-001 | When user clicks "Add to Cart" button, the system shall add the item to cart and update cart count |
| CART-EV-002 | When user clicks "Remove" on a cart item, the system shall remove the item and recalculate total |
| CART-EV-003 | When user clicks "Checkout", the system shall validate cart contents and redirect to payment |
| CART-ST-001 | If cart is empty, then the system shall display "Your cart is empty" message and hide checkout button |
| CART-ST-002 | If user is not logged in, then the system shall save cart to local storage |
| CART-CX-001 | When user adds item to cart, and item is out of stock, the system shall display "Out of stock" and prevent addition |
| CART-OP-001 | Where user has items in cart, the user can apply a discount code |

---

## Example 3: File Upload

### Original Requirement
"Allow uploading documents"

### EARS Conversion

| ID | Requirement |
|----|-------------|
| FILE-EV-001 | When user selects a file and clicks Upload, the system shall upload the file and display progress |
| FILE-EV-002 | When upload completes, the system shall display success message and file preview |
| FILE-ST-001 | If file exceeds 10MB, then the system shall reject upload with size error |
| FILE-ST-002 | If file type is not PDF/DOC/DOCX, then the system shall reject with format error |
| FILE-CX-001 | When user uploads file, and network connection is lost, the system shall pause upload and offer resume option |
| FILE-UB-001 | The system shall scan all uploaded files for viruses before storage |
