# Introduction

Best practices are broken down in various categories:

- General programming
- Exception handling
- Resources management
- Design
- Transaction management
- Performance
- Security

## Purpose

Coding best practices are practices that, when understood and applied, ensure:

- Reduction of code defects (of various kinds and severity levels) by showing common coding traps and mistakes and ways to avoid them
- Increase of code readability and maintainability by providing a common and consistent way to code across team members and even across projects.

Understanding and applying these coding best practices will help every engineer become a better programmer.

## Sources of coding best practices

The content of this document is mainly sourced from results of recurrent defects analysis performed by Code Inspection teams. This is the main source for adding new Best Practices to this document.
Another source is contractor feedback on code created by engineers.
Finally, comments, provided by engineers themselves, based on their effective experience in projects, also help ameliorate this document by improving existing Best Practices.

## How to use the document

### Reading for the first time

If you are a newcomer, you have to read this document during the first month trial. There are many best practices in the document to read; hence you are advised to read three best practices every day. Thus, you can cover all practices by the end of the first month. Best Practices are not supposed to be learned “by heart”.

### Use as a reference

When you already have read this document, you can refer to it at any time during coding when you face a problem which you remember is addressed in one Best Practice (you can quickly look up best practices in the Table of Contents).

# General programming

### **BP201: Prefer for-each to traditional loops**

**Problem**: Traditional loops are error-prone for iterating over collections

**Type**: Reliability

**Severity**: Serious

**Bad code / behavior**:

```java
public Region findRegionByLocation(String locationName) {
    for (int i = 0; i < regionalData.getAllRegions().size(); i++) {
        Region region = regionalData.getAllRegions().get(i);
        Iterator<Country> countryIt = region.getCountries().iterator();
        while (countryIt.hasNext()) {
            Country country = countryIt.next();
            Iterator<Location> locationIt = country.getLocations().iterator();
            while (locationIt.hasNext()) {
                Location location = locationIt.next();
                if (StringUtils.equals(locationName, location.getLocationName())) {
                    return region;
                }
            }
        }
    }
    return null;
}
```

**Description**: Once being used in traditional loops, the iterator and index variable occur many times in each loop, which resulting in chances to get them wrong. The for-each loop gets rid of the opportunity for error by hiding the iterator or index variable completely. Its advantages over the traditional become greater when there are nested iterations over multiple collections.

**Good code / behavio**r: 

```java
public Region findRegionByLocation(String locationName) {
    for (Region region : regionalData.getAllRegions()) {
        for (Country country : region.getCountries()) {
            for (Location location : country.getLocations()) {
                if (StringUtils.equals(locationName, location.getLocationName())) {
                    return region;
                }
            }
        }
    }
    return null;
}
```

### **BP202: Do not use Assert at all in the code except for unit tests**

**Problem**: Assert is not designed for business checking

**Type**: Reliability

**Severity**: Serious

**Bad code / behavior**: 

```java
@Override
public Employee save(Employee employee) {
    if (employee.isNew()) {
        Department department = employee.getDepartment();
        assert (employeeDao.countByName(employee.getFirstName()) > 0);
        assert (departmentDao.countEmployee(department.getDepartmentId()) > department.getMaxEmployee());
    }
    return employeeDao.saveOrUpdate(employee);
}
```

**Description**: 

Assert should not be used in place of business checking because of the following reasons:

- it is means for programming error check and not for business code
- it can be turned off and cause the validation to fail
- it cannot throw specific Exception

**Good code / behavior**: 

```java
@Override
public Employee save(Employee employee) throws EmployeeNameExistedException, DepartmentFullException {
    if (employee.isNew()) {
        if (employeeDao.countByName(employee.getFirstName()) > 0) {
            throw new EmployeeNameExistedException("Employee name existed", employee.getFirstName());
        }
 
        Department department = employee.getDepartment();
        if (departmentDao.countEmployee(department.getDepartmentId()) > department.getMaxEmployee()) {
            throw new DepartmentFullException("Department is full", department.getDepartmentName(),
                    department.getMaxEmployee());
        }
    }
    return employeeDao.saveOrUpdate(employee);
}
```

**Comment**: 

This is true for public methods, but for private, you can still use assertions (because that would be programming errors here)
Whenever consider to use the Assertions we should think to provided utilities like Spring’s Assertion or Apache’s Validate or Guava’s Preconditions.

```java
Spring’s Assertion.
Assert.notNull(clazz, "The class must not be null");
Assert.isTrue(i > 0, "The value must be greater than zero");
 
Guava’s Preconditions
Preconditions.checkArgument(input != null, "Input must be not null!");
 
Apache’s Validate
Validate.notNull(clazz, "The class must not be null");
Validate.isTrue(i > 0, "The value must be greater than zero");
```

### **BP203: Be careful of “TODO” leftovers in code**

**Problem**: Nobody knows what “to do” with TODO comments left in code

**Type**: Maintainability

**Severity**: Serious

**Bad code / behavior**: 

```java
@Override
public Employee save(Employee employee) throws EmployeeNameExistedException, DepartmentFullException {
    // TODO: rework the check later on.
    if (employee.isNew()) {
        if (employeeDao.countByName(employee.getFirstName()) > 0) {
            throw new EmployeeNameExistedException("Employee name existed", employee.getFirstName());
        }
 
        Department department = employee.getDepartment();
        if (departmentDao.countEmployee(department.getDepartmentId()) > department.getMaxEmployee()) {
            throw new DepartmentFullException("Department is full", department.getDepartmentName(),
                    department.getMaxEmployee());
        }
    }
    return employeeDao.saveOrUpdate(employee);
}
 
@Override
public Employee delete(Employee employee) {
    // TODO Auto-generated method stub
    return null;
}
```

**Description**: 

Many times, developers keep auto-generated TODO comments in their code even though they have filled in the matching code block adequately.
Also, sometimes, developers add some TODO comments just to remember that they should review some code part later on; however, very often, those TODO comments are never performed by the author and are kept for a long time. Later on, when they are discovered, nobody remembers what to do because those comments are not clear enough about the task to be performed and the necessary conditions to perform that task.

**Good code / behavior**: 

Every **TODO** comment should clearly specify the following information:

- **Who**: the visa of the author of this TODO comment (and probably the one who will have to perform this task later on)
- **When**: the latest date (or milestone) at which this task should be performed in the code; this must also include conditions (if any) for this task to be start-able (e.g. external constraint, library version change…)
- **Why**: the reason why we plan a change in the code and it can’t be done right now; this can help, later on, figuring out how important this task is for the project.
- **What**: a good (detailed) description of the task that must be performed (so that it can be performed by someone else than the comment’s author if needed). This could include important advice (or warning) if there is some risk changing that code.

```java
@Override
public Employee save(Employee employee) throws EmployeeNameExistedException, DepartmentFullException {
    // TODO [JPO/IT2]: add validation to ensure that the employee
    // has correct commission value. Confirmation from CH is required.
    if (employee.isNew()) {
        if (employeeDao.countByName(employee.getFirstName()) > 0) {
            throw new EmployeeNameExistedException("Employee name existed", employee.getFirstName());
        }
 
        Department department = employee.getDepartment();
        if (departmentDao.countEmployee(department.getDepartmentId()) > department.getMaxEmployee()) {
            throw new DepartmentFullException("Department is full", department.getDepartmentName(),
                    department.getMaxEmployee());
        }
    }
    return employeeDao.saveOrUpdate(employee);
}
 
@Override
public Employee delete(Employee employee) {
    // TODO [JPO/IT3]: implements this service when the Abacus webservice is
    // ready.
    return null;
}
```

### **BP204: Comparing two objects (“.equals” instead of “==”)**

**Problem**: Operator “==” actually compares the two object references and may result in wrong behavior

**Type**: Functionality

**Severity**: Serious

**Bad code / behavior**: 

```java
public void removeEmployee(String visa) {
    Employee employeeToBeRemoved = null;
    for (Employee employee : employees) {
        if (employee.getVisa() == visa) {
            employeeToBeRemoved = employee;
            break;
        }
    }
    employees.remove(employeeToBeRemoved);
}
```

**Description**: 

When using operator “**==**”, the two involved object references are compared to see if they point to the same memory location.

In Java, we cannot compare, for example, two instances of **`java.lang.String`** for equality with **`==`**. We must instead use the **`equals()`** method, which is inherited by all classes from **`java.lang.Object`**.

In C#, the same principle hold, **`except`** for strings themselves, where `==` is equivalent to **`Equals`**.

**Good code / behavior**: 

```java
public void removeEmployee(String visa) {
    Employee employeeToBeRemoved = null;
    for (Employee employee : employees) {
        if (employee.getVisa().equals(visa)) {
            employeeToBeRemoved = employee;
            break;
        }
    }
    employees.remove(employeeToBeRemoved);
}
```

### **BP205: Always invoke equals() on constants when comparing them with variables**

**Problem**: NullPointerException may occur if equals() is invoked on variables when comparing them with constants

**Type**: Reliability

**Severity**: Non critical

**Bad code / behavior**: 

```java
private void initAction() {
    txtFirstName.addFocusListener(new FocusListener() {
         
        @Override
        public void focusLost(FocusEvent e) {
            if (!txtFirstName.getText().equals(Constants.EMPTY_STRING)) {
                employeeModel.getData().setFirstName(txtFirstName.getText());
            }
        }
         
        @Override
        public void focusGained(FocusEvent e) {
            // Does nothing.
        }
    });
}
```

**Description**:  NullPointerException will occur in method initAction() above if txtFirstName.getText() returns null. Thus, it is a good practice to compare strings as shown below, that doesn’t result in an exception if the variable is indeed null.

**Good code / behavior**: 

```java
private void initAction() {
    txtFirstName.addFocusListener(new FocusListener() {
         
        @Override
        public void focusLost(FocusEvent e) {
            if (!Constants.EMPTY_STRING.equals(txtFirstName.getText())) {
                employeeModel.getData().setFirstName(txtFirstName.getText());
            }
        }
         
        @Override
        public void focusGained(FocusEvent e) {
            // Does nothing.
        }
    });
}
```

### **BP206: Minimize the scope of local variables**

**Problem**: Extending local variables’ scope reduces readability, maintainability and increase the likelihood of error

**Type**: Maintainability

**Severity**: Non critical

**Bad code / behavior**: N/A

**Description**: 

Declaring a local variable prematurely can cause its scope not only to extend too early, but also to end too late. The scope of a local variable extends from the point where it is declared to the end of the enclosing block. If a variable is declared outside of the block in which it is used, it remains visible after the program exits that block. If a variable is used accidentally before or after its region of intended use, the consequences can be disastrous.

Thus, it is advised to declare the variable just before used. By minimizing the scope of local variables, you increase the readability and maintainability of your code and reduce the likelihood of error.

**Good code / behavior**: N/A

### **BP207: Hard-coded value**

**Problem**: Nobody understand meaning of a hard-coded value (especially for business value). When the same value is hard-coded on several places, it is the source of the code duplication and regression.

**Type**: Maintainability

**Severity**: Serious

**Bad code / behavior**: 

```java
public class UserValidator {
     
    public void validate(User user) {
        List<ValidationFieldError> fieldErrors = new ArrayList<>();
             
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
             
if (StringUtils.isNotBlank(user.getEmail())
                && !pattern.matcher(user.getEmail()).matches()) {
            addFieldError("userEmail", "validation.rule.email.invalid", fieldErrors);
        }
    }
}
     
public class CompanyValidator {
     
    public void validate(Company company) {
        List<ValidationFieldError> fieldErrors = new ArrayList<>();
             
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
             
        if (StringUtils.isNotBlank(company.getEmail())
            && !pattern.matcher(company.getEmail()).matches()) {
            addFieldError("companyEmail", "validation.rule.email.invalid", fieldErrors);
    }
    }
}
```

**Description**: 

Hard-coding a value inside application makes it hard for readers to understand its meaning and reduces software’s readability.

Especially in case the value representing the same business or technical meaning is hard-coded on several places, it also becomes a source of code duplication and regression.

Furthermore, whenever we want to update the value, it is very likely that we will forget to update some occurrences.

The solution for this problem is to extract the hard-coded value into a meaningful constants and use this whenever we want to access the value at every place in the system.

**Good code / behavior**: 

```java
public interface ValidationConstants {
         
    public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
     
    public static final String INVALID_MAIL_MSG_KEY = "validation.rule.email.invalid";     
}
public class UserValidator {
     
    public void validate(User user) {
        List<ValidationFieldError> fieldErrors = new ArrayList<>();
             
        Pattern pattern = Pattern.compile(ValidationConstants.EMAIL_PATTERN);
             
if (StringUtils.isNotBlank(user.getEmail())
                && !pattern.matcher(user.getEmail()).matches()) {
            addFieldError("userEmail", ValidationConstants.INVALID_MAIL_MSG_KEY, fieldErrors);
        }
    }
}
     
public class CompanyValidator {
     
    public void validate(Company company) {
        List<ValidationFieldError> fieldErrors = new ArrayList<>();
             
        Pattern pattern = Pattern.compile(ValidationConstants.EMAIL_PATTERN);
             
        if (StringUtils.isNotBlank(company.getEmail())
            && !pattern.matcher(company.getEmail()).matches()) {
            addFieldError("companyEmail", ValidationConstants.INVALID_MAIL_MSG_KEY, fieldErrors);
    }
    }
}
```

**Comment**: 

“Magic number” is also a subset of the “hardcoded value” issue, where we really don’t know what the meaning of the value is.

**BAD CODE**

```java
red = Math. min(red + 25, 255);
```

**GOOD CODE**

```java
final int ADDED_RED = 25;
final int MAX_RED = 255;
         
red = Math.min(red + ADDED_RED, MAX_RED);
```

# Exception handling

### **BP301: Never leave empty catch block**

**Problem**: Ignoring an exception will result in no way to trace back to the cause of the problem once it occurs

**Type**: Reliability

**Severity**: Critical

**Bad code / behavior**: 

```java
@Override
public void exportToCSV(String filePath) {
    List<Employee> employees = employeeDao.getAll();
    StringBuilder content = new StringBuilder();
    for (Employee employee : employees) {
        content.append(employee.asCSVFormat());
        content.append(LINE_SEPARATOR);
    }
    FileWriter fw = null;
    try {
        fw = new FileWriter(new File(filePath));
        fw.write(content.toString());
        fw.flush();
    } catch (IOException e) {
         
    } finally {
        if (fw != null) {
            try {
                fw.close();
            } catch (IOException e) {
 
            }
        }
    }
}
```

**Description**: 

Ignoring an exception is analogous to ignoring a fire-alarm and will result in:

- an application silently continues in case of error
- no easy way to trace back to original source of error
In normal case (first case in the example above), it is advised to:
- log the exception in “error” level to ensure that the exception is traced back in case client code forgets to do that
- propagate the original, or respectively translate and then propagate the translated exception outward to help preserving information to aid in debugging the failure
At the very least (second case in the example above), the catch block should contain a log of the exception to help investigating the matter if the exception happens more often.

**Good code / behavior**: 

```java
@Override
public void exportToCSV(String filePath) {
    List<Employee> employees = employeeDao.getAll();
    StringBuilder content = new StringBuilder();
    for (Employee employee : employees) {
        content.append(employee.asCSVFormat());
        content.append(LINE_SEPARATOR);
    }
    FileWriter fw = null;
    try {
        fw = new FileWriter(new File(filePath));
        fw.write(content.toString());
        fw.flush();
    } catch (IOException e) {
        LOGGER.error("Error occured when writing file", e);
        throw new RuntimeException("Error occured when writing file", e);
    } finally {
        if (fw != null) {
            try {
                fw.close();
            } catch (IOException e) {
                LOGGER.error("Error occured when closing file", e);
            }
        }
    }
}
```

### **BP302: Be specific when throwing exception**

**Problem**: It is difficult for client code to deal properly with “*generic*” exceptions

**Type**: Reliability

**Severity**: Serious

**Bad code / behavior**: 

```java
@Override
public Employee save(Employee employee) throws Exception {
    if (employee.isNew()) {
        if (employeeDao.countByName(employee.getFirstName()) > 0) {
            throw new Exception("Employee name existed: "
                    + employee.getFirstName());
        }
 
        Department department = employee.getDepartment();
        if (departmentDao.countEmployee(department.getDepartmentId()) > department
                .getMaxEmployee()) {
            throw new Exception("Department is full: "
                    + department.getDepartmentName());
        }
    }
    return employeeDao.saveOrUpdate(employee);
}
```

**Description**: In above example, it is difficult for client code to figure out which specific error has occurred

**Good code / behavior**: 

```java
@Override
public Employee save(Employee employee)
        throws EmployeeNameExistedException, DepartmentFullException {
    if (employee.isNew()) {
        if (employeeDao.countByName(employee.getFirstName()) > 0) {
            throw new EmployeeNameExistedException("Employee name existed: "
                    + employee.getFirstName());
        }
 
        Department department = employee.getDepartment();
        if (departmentDao.countEmployee(department.getDepartmentId()) > department
                .getMaxEmployee()) {
            throw new DepartmentFullException("Department is full: "
                    + department.getDepartmentName());
        }
    }
    return employeeDao.saveOrUpdate(employee);
}
```

### **BP303: Include information about failure when throwing exception**

**Problem**: Lacking of knowledge about the failure may create difficulty in handling it

**Type**: Reliability

**Severity**: Serious

**Bad code / behavior**: 

**Exception definition:**

```java
public class DepartmentFullException extends Exception {
 
    public DepartmentFullException(String message) {
        super(message);
    }
}
 
public class EmployeeNameExistedException extends Exception {
     
    public EmployeeNameExistedException(String message) {
        super(message);
    }
}
```

**Service code:**

```java
@Service
public class EmployeeServiceImpl implements EmployeeService {
 
    private static final Logger LOGGER = LoggerFactory
            .getLogger(EmployeeServiceImpl.class);
 
    private static final String LINE_SEPARATOR = System
            .getProperty("line.separator");
 
    @Inject
    private EmployeeDao employeeDao;
 
    @Inject
    private DepartmentDao departmentDao;
 
    @Override
    public Employee save(Employee employee) throws EmployeeNameExistedException,
            DepartmentFullException {
        if (employee.isNew()) {
            if (employeeDao.countByName(employee.getFirstName()) > 0) {
                throw new EmployeeNameExistedException("Employee name existed: "
                        + employee.getFirstName());
            }
 
            Department department = employee.getDepartment();
            if (departmentDao.countEmployee(department.getDepartmentId()) > department
                    .getMaxEmployee()) {
                throw new DepartmentFullException("Department is full: "
                        + department.getDepartmentName());
            }
        }
        return employeeDao.saveOrUpdate(employee);
    }
```

**Presentation code:**

```java
@Component
public class EmployeeDetailModel {
 
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeDetailModel.class);
 
    @Inject
    private EmployeeService employeeService;
 
    private Employee data;
 
    public void save() {
        try {
            employeeService.save(data);
        } catch (EmployeeNameExistedException e) {
            LOGGER.error("Employee name does not exist", e);
            DialogHelper.showErrorDialog(I18nHelper.getText("EmployeeNameExistedException"));
        } catch (DepartmentFullException e) {
            LOGGER.error("Department is full", e);
            DialogHelper.showErrorDialog(I18nHelper.getText("DepartmentFullException"));
        }
    }
```

**Description**: In above example, it’s difficult for client code to figure out what action should be carried out next, since there is no or only a little hint from the exception

**Good code / behavior:** 

**Exception definition:**

```java
public class DepartmentFullException extends Exception {
 
    private String departmentName;
    private int maxEmployee;
 
    public DepartmentFullException(String message, String departmentName, int maxEmployee) {
        super(message);
        this.departmentName = departmentName;
        this.maxEmployee = maxEmployee;
    }
 
    public DepartmentFullException(String message) {
        super(message);
    }
 
    public String getDepartmentName() {
        return departmentName;
    }
 
    public int getMaxEmployee() {
        return maxEmployee;
    }
}
 
public class EmployeeNameExistedException extends Exception {
     
    private String violatedName;
 
    public EmployeeNameExistedException(String message, String violatedName) {
        super(message);
        this.violatedName = violatedName;
    }
 
    public EmployeeNameExistedException(String message) {
        super(message);
    }
 
    public String getViolatedName() {
        return violatedName;
    }
}
```

**Service code:**

```java
public class EmployeeServiceImpl implements EmployeeService {
 
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeServiceImpl.class);
 
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
 
    @Inject
    private EmployeeDao employeeDao;
 
    @Inject
    private DepartmentDao departmentDao;
 
    @Override
    public Employee save(Employee employee) throws EmployeeNameExistedException, DepartmentFullException {
        if (employee.isNew()) {
            if (employeeDao.countByName(employee.getFirstName()) > 0) {
                throw new EmployeeNameExistedException("Employee name existed", employee.getFirstName());
            }
 
            Department department = employee.getDepartment();
            if (departmentDao.countEmployee(department.getDepartmentId()) > department.getMaxEmployee()) {
                throw new DepartmentFullException("Department is full", department.getDepartmentName(),
                        department.getMaxEmployee());
            }
        }
        return employeeDao.saveOrUpdate(employee);
    }
```

**Presentation code:**

```java
@Component
public class EmployeeDetailModel {
 
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeDetailModel.class);
 
    @Inject
    private EmployeeService employeeService;
 
    private Employee data;
 
    public void save() {
        try {
            employeeService.save(data);
        } catch (EmployeeNameExistedException e) {
            String errorMsg = I18nHelper.getText("EmployeeNameExistedException", e.getViolatedName());
            LOGGER.error(errorMsg);
            DialogHelper.showErrorDialog(errorMsg);
        } catch (DepartmentFullException e) {
            String errorMsg = I18nHelper.getText("DepartmentFullException", e.getDepartmentName(),
                    e.getMaxEmployee());
            LOGGER.error(errorMsg);
            DialogHelper.showErrorDialog(errorMsg);
        }
    }
```

### **BP304: Always put try..catch in finally (if needed)**

**Problem**: Logging information of unusual error may be lost if try..catch is forgotten in finally

**Type**: Reliability

**Severity**: Serious

**Bad code / behavior**: 

```java
List<Employee> employees = employeeDao.getAll();
String content = "";
for (Employee employee : employees) {
    content += employee.asCSVFormat();
    content += LINE_SEPARATOR;
}
FileWriter fw = null;
try {
    fw = new FileWriter(new File(filePath));
    fw.write(content.toString());
    fw.flush();
} catch (IOException e) {
    LOGGER.error("Error occured when writing file", e);
    throw new RuntimeException("Error occured when writing file", e);
} finally {
    if (fw != null) {
        fw.close();
    }
}
```

**Description**: If methods invoked from within finally clause can themselves throw exceptions, they must be wrapped inside a try..catch block with adequate logging. This will help to log and analyze any problem that happens when closing the resource (although this is unusual).

**Good code / behavior**: 

```java
List<Employee> employees = employeeDao.getAll();
String content = "";
for (Employee employee : employees) {
    content += employee.asCSVFormat();
    content += LINE_SEPARATOR;
}
FileWriter fw = null;
try {
    fw = new FileWriter(new File(filePath));
    fw.write(content.toString());
    fw.flush();
} catch (IOException e) {
    LOGGER.error("Error occured when writing file", e);
    throw new RuntimeException("Error occured when writing file", e);
} finally {
    if (fw != null) {
        try {
            fw.close();
        } catch (IOException e) {
            LOGGER.error("Error occured when closing file", e);
        }
    }
}
```

### **BP305: Always log exception with appropriate severity**

**Problem**: Important logging information can be lost when system log level is changed.

**Type**: Reliability

**Severity**: Serious

**Bad code / behavior**: 

```java
protected void Page_Load(object sender, EventArgs args)
{
    try
    {
       ProcessImportData();
    }
    catch (ExceptionThatNeedImmediateAttention e)
    {
       Logger.Debug("Critical error occured while import employee data", e);
       ShowErrorMessage();
    }
}
```

**Description**: Although the above exception stack trace is required for determine issues with the application, it is logged with DEBUG level. This log level is unlikely to be used in productive systems; therefore the information can be lost forever.

**Good code / behavior**: 

Always log the information with its respective severity. It’s always good to check if the specified level is enabled or not to reduce the computational cost to construct log messages. 

```java
protected void Page_Load(object sender, EventArgs args)
    {
        try
        {
            ProcessImportData();
        }
        catch (ExceptionThatNeedImmediateAttention e)
        {
            if (Logger.IsErrorEnabled)
            {
                Logger.Error("Critical error occured while import employee data", e);
            }
            ShowErrorMessage();
        }
    }
```

# Resources management

### **BP401: All resources must be closed after used**

**Problem**: Forgetting to properly close a resource after used will cause it to be exhausted

**Type**: Efficiency

**Severity**: Critical

**Bad code / behavior**: 

```java
@Override
public void exportToCSV(String filePath) {
    List<Employee> employees = employeeDao.getAll();
    StringBuilder content = new StringBuilder();
    for (Employee employee : employees) {
        content.append(employee.asCSVFormat());
        content.append(LINE_SEPARATOR);
    }
    FileWriter fw = null;
    try {
        fw = new FileWriter(new File(filePath));
        fw.write(content.toString());
        fw.flush();
    } catch (IOException e) {
        LOGGER.error("Error occured when writing file", e);
        throw new RuntimeException("Error occured when writing file", e);
    }
}
```

**Description**: 

Expensive resources must always be cleaned up explicitly, by invoking the **close()** method from the **finally** block that surrounding its initializing place. Otherwise, these resources will soon be exhausted and cause serious consequences to the functionality/availability/scalability of the application.

Here is a list of some resources that are considered expensive:

- Streams (both input and output)
- Readers
- Writers
- Database connections
- SQL statements
- Transaction
- Hibernate session

**Good code / behavior**: 

```java
@Override
public void exportToCSV(String filePath) {
    List<Employee> employees = employeeDao.getAll();
    StringBuilder content = new StringBuilder();
    for (Employee employee : employees) {
        content.append(employee.asCSVFormat());
        content.append(LINE_SEPARATOR);
    }
    FileWriter fw = null;
    try {
        fw = new FileWriter(new File(filePath));
        fw.write(content.toString());
        fw.flush();
    } catch (IOException e) {
        LOGGER.error("Error occured when writing file", e);
        throw new RuntimeException("Error occured when writing file", e);
    } finally {
        if (fw != null) {
            try {
                fw.close();
            } catch (IOException e) {
                LOGGER.error("Error occured when closing file", e);
            }
        }
    }
}
```

NOTE: 

- In C#, “using” keyword can be used to replace try ... finally to dispose resource.
- In Java 7+, the try-with-resources statement could be used to close the resources regardless of whether the try statement completes normally or abruptly: [http://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html](http://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html)

**Comment**: 

The following approach which delegates the closing task to a helper should be avoided as some of the available libraries (like [org.apache.commons.io](http://org.apache.commons.io/).IOUtils) just silently ignore any exception might happen. 

```java
@Override
public void exportToCSV(String filePath) {
    List<Employee> employees = employeeDao.getAll();
    StringBuilder content = new StringBuilder();
    for (Employee employee : employees) {
        content.append(employee.asCSVFormat());
        content.append(LINE_SEPARATOR);
    }
    FileWriter fw = null;
    try {
        fw = new FileWriter(new File(filePath));
        fw.write(content.toString());
        fw.flush();
    } catch (IOException e) {
        LOGGER.error("Error occured when writing file", e);
        throw new RuntimeException("Error occured when writing file", e);
    } finally {
        IOUtils.closeQuietly(fw);
    }
}
```

### **BP402: Don’t do lazy initialization unless you need to**

**Problem**: Lazy initialization is used for optimizations only and becomes tricky in the presence of multiple threads

**Type**: Efficiency

**Severity**: Critical

**Bad code / behavior**: 

```java
public class EmployeePanel extends JPanel {
 
    private JLabel lblFirstName;
    private JLabel lblLastName;
    private JTextField txtFirstName;
    private JTextField txtLastName;
 
    public EmployeePanel() {
        initUI();
    }
 
    private void initUI() {
        setLayout(new FlowLayout());
        add(getLblFirstName());
        add(getTxtFirstName());
        add(getLblLastName());
        add(getTxtLastName());
    }
 
    private JTextField getTxtFirstName() {
        if (txtFirstName == null) {
            txtFirstName = new JTextField();
        }
        return txtFirstName;
    }
 
    private JTextField getTxtLastName() {
        if (txtLastName == null) {
            txtLastName = new JTextField();
        }
        return txtLastName;
    }
 
    private JLabel getLblFirstName() {
        if (lblFirstName == null) {
            lblFirstName = new JLabel(I18nHelper.getString("EmployeePanel.lblFirstName"));
        }
        return lblFirstName;
    }
 
    private JLabel getLblLastName() {
        if (lblLastName == null) {
            lblLastName = new JLabel(I18nHelper.getString("EmployeePanel.lblLastName"));
        }
        return lblLastName;
    }
}
```

**Description**: 

Lazy initialization is the act of delaying the initialization of a field until its value is needed. It is a double-edged sword while decreasing the cost of initializing a class or creating an instance, at the expense of increasing the cost of accessing the lazily initialized field.

Especially, if two or more threads share a lazily initialized field, it is critical that some form of synchronization be employed, or severe bugs can result.

Thus, under most circumstances, normal initialization is preferable to lazy initialization.

**Good code / behavior**: 

```java
public class EmployeePanel extends JPanel {
 
    private JLabel lblFirstName = new JLabel(I18nHelper.getString("EmployeePanel.lblFirstName"));
    private JLabel lblLastName = new JLabel(I18nHelper.getString("EmployeePanel.lblLastName"));
    private JTextField txtFirstName = new JTextField();
    private JTextField txtLastName = new JTextField();
 
    public EmployeePanel() {
        initUI();
    }
 
    private void initUI() {
        setLayout(new FlowLayout());
        add(lblFirstName);
        add(txtFirstName);
        add(lblLastName);
        add(txtLastName);
    }
}
```

**Comment**:  When lazy initialization of an instance variable is unavoidable, double-checked-locking mechanism must be used (see the “*Use double-check idiom when lazy-initializing object in threaded context*” best practice)

### **BP403: Use double-check idiom when lazy-initializing object in threaded context**

**Problem**: If two or more threads share a lazily initialized field, it is critical that some form of synchronization be employed, or severe bugs can result

**Type**: Efficiency

**Severity**: Critical

**Bad code / behavior**: 

```java
@Component
public class EmployeeImportBatch {
     
    private EmployeeCache employeeCache = null;
     
    public EmployeeCache getEmployeeCache() {
        if (employeeCache == null) {
            synchronized (this) {
                employeeCache = new EmployeeCache();
            }
        }
        return employeeCache;
    }
```

**Description**: 

Supposed more than one thread arrive at the getEmployeeCache() method. All the threads finds the employeeCache to be null and they will respectively take turn to initialize the expensive cache, which is not desired.

A better way is to check employeeCache again in locking mode to see if some other thread has already initialized it.

**Good code / behavior**: 

```java
@Component
public class EmployeeImportBatch {
     
    private volatile EmployeeCache employeeCache = null;
     
    public EmployeeCache getEmployeeCache() {
        EmployeeCache result = employeeCache;
        if (result == null) {
            synchronized (this) {
                result = employeeCache;
                if (result == null) {
                    employeeCache = result = new EmployeeCache();
                }
            }
        }
        return employeeCache;
    }
```

**Pitfall**:  Double-check idiom does not work reliably with all JVM (1.5+ only)! But at minimum, should do the “single checked locking” (i.e. put “synchronized” at method declaration)

# Design

### **BP501: Never store information in stateless components**

**Problem**: Storing information in stateless components will make them become stateful and cause serious problems in multi thread environment

**Type**: Functionality

**Severity**: Critical

**Bad code / behavior**: 

```java
@Service
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class EmployeeServiceImpl implements EmployeeService {
 
    @Inject
    private DepartmentDao departmentDao;
     
    private BigDecimal defaultCommission;
 
    @Override
    public Employee update(Employee employee) {
        if (employee.getCommision() == null) {
            defaultCommission = departmentDao.getDefaultCommission(employee.getDepartment().getDepartmentId());
            employee.setCommision(calculateCommission(employee, defaultCommission));
        }
        employeeDao.update(employee);
        return employee;
    }
```

**Description**: As EmployeeServiceImpl is a stateless service and used in a multi thread environment, there is chance that multiple threads access method update() at the same time and modify value of the shared private attribute defaultCommision that is being read by other threads.

**Good code / behavior**: 

```java
@Service
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class EmployeeServiceImpl implements EmployeeService {
 
    @Inject
    private DepartmentDao departmentDao;
 
    @Override
    public Employee update(Employee employee) {
        if (employee.getCommision() == null) {
            BigDecimal defaultCommission = departmentDao.getDefaultCommission(employee.getDepartment()
                    .getDepartmentId());
            employee.setCommision(calculateCommission(employee, defaultCommission));
        }
        employeeDao.update(employee);
        return employee;
    }
```

### **BP503: Mark classes, methods as final if they should not be extended/overridden**

**Problem**: Classes/methods whose behaviors are fixed but could still be technically extended/overridden will lead to unexpected and wrong behaviors

**Type**: Maintainability

**Severity**: Serious

**Bad code / behavior**: 

```java
**public abstract class AbstractFtpImportImpl<T extends Serializable> implements FtpImport<T> {
 
    private Logger logger = LoggerFactory.getLogger(getClass());
 
    private GenericDao<T, Long> dao;
    private NotificationService notificationService;
 
    @Override
    public void doImport() {
        Exception error = null;
        try {
            List<File> files = download();
            List<T> data = extractData(files);
            doImportInternal(data);
        } catch (Exception e) {
            error = e;
            logger.error("Unexpected error occured during import files", e);
            throw new RuntimeException("Unexpected error occured during import files", e);
        } finally {
            log(error);
        }
    }
 
    protected abstract List<File> download();
 
    protected abstract List<T> extractData(List<File> files);
 
    protected void doImportInternal(List<T> data) {
        for (T t : data) {
            dao.create(t);
        }
    }
 
    private void log(Exception error) {
        if (error == null) {
            notificationService.succeed();
        } else {
            notificationService.error(error);
        }
    }
}**
```

**Description**: Method doImport() might be mistakenly changed in subclasses and cause undesired effect, e.g. forgetting to send notification after the import finishes.

**Good code / behavior**: 

```java
public abstract class AbstractFtpImportImpl<T extends Serializable> implements FtpImport<T> {
 
    private Logger logger = LoggerFactory.getLogger(getClass());
 
    private GenericDao<T, Long> dao;
    private NotificationService notificationService;
 
    @Override
    public final void doImport() {
        Exception error = null;
        try {
            List<File> files = download();
            List<T> data = extractData(files);
            doImportInternal(data);
        } catch (Exception e) {
            error = e;
            logger.error("Unexpected error occured during import files", e);
            throw new RuntimeException("Unexpected error occured during import files", e);
        } finally {
            log(error);
        }
    }
 
    protected abstract List<File> download();
 
    protected abstract List<T> extractData(List<File> files);
 
    protected void doImportInternal(List<T> data) {
        for (T t : data) {
            dao.create(t);
        }
    }
 
    private void log(Exception error) {
        if (error == null) {
            notificationService.succeed();
        } else {
            notificationService.error(error);
        }
    }
}
```

NOTE: In C#, use "sealed" keyword instead of "final". Besides, non-static methods in C# are sealed by default (different than in Java).

**Pitfall**: Marking a method/class as final hinders extensibility. In libraries or frameworks, be very careful because not all cases might have been foreseen. If it happens, it causes a lot of troubles since the final classes or methods cannot be extended.

### **BP504: Prefer empty list, array over null object when designing API with collection returning type**

**Problem**: Returning NULL object for collection type always makes client code unnecessarily complex

**Type**: Maintainability

**Severity**: Serious

**Bad code / behavior**: 

**Service code:**

```java
@Service
@Scope(BeanDefinition.SCOPE_SINGLETON)
@Transactional
public class EmployeeServiceImpl implements EmployeeService {
 
    @Override
    public List<Employee> getAll() {
        if (!getCurrentUser().isManager()) {
            return null;
        }
        return employeeDao.getAll();
    }
 
    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
```

**Presentation code:**

```java
@Component
public class EmployeeListModel {
 
    @Inject
    private EmployeeService employeeService;
 
    public HSSFWorkbook exportToExcel() {
        List<Employee> allEmployees = employeeService.getAll();
 
        HSSFWorkbook result = null;
        if (allEmployees != null) {
            result = createWorkBook();
            for (Employee employee : allEmployees) {
                writeEmployee(employee, result);
            }
        }
        return result;
    }
```

**Description**: Returning NULL object for collection type always makes client code unnecessarily complex as a nullify check must to be applied before working with the result

**Good code / behavior**: 

**Service code:**

```java
@Service
@Scope(BeanDefinition.SCOPE_SINGLETON)
@Transactional
public class EmployeeServiceImpl implements EmployeeService {
 
    @Override
    public List<Employee> getAll() {
        if (!getCurrentUser().isManager()) {
            return java.util.Collections.EMPTY_LIST;
        }
        return employeeDao.getAll();
    }
```

**Presentation code:**

```java
@Component
public class EmployeeListModel {
 
    @Inject
    private EmployeeService employeeService;
 
    public HSSFWorkbook exportToExcel() {
        List<Employee> allEmployees = employeeService.getAll();
 
        HSSFWorkbook result = createWorkBook();
        for (Employee employee : allEmployees) {
            writeEmployee(employee, result);
        }
        return result;
    }
```

### **BP505: The constants and configurable parameters are different**

**Problem**: Although they are different concepts, constants are mistakenly used as configurable parameters and vice versa

**Type**: Maintainability

**Severity**: Serious

**Bad code / behavior**: 

```java
@Component
public class EmployeeImportBatch {
 
    private static final String WS_URL = "http://example.vn/services";
    private static final String WS_USER_NAME = "username";
    private static final String WS_PASSWORD = "password";
     
    @Value(value = "#{systemProperties.wsSoapVersion}")
    private String wsSoapVersion;
```

**Description**: 

If a value will be never changed, or if it changes, we have to rebuild the system. Then put it as constants.

Else make it as configurable parameters, so that it can be configured when the system is deployed in different environment (development, test, production, .etc.).

**Good code / behavior**: 

```java
@Component
public class EmployeeImportBatch {
 
    private static final String WS_SOAP_VERSION = "org.springframework.ws.soap.SoapVersion.SOAP_11";
 
    @Value(value = "#{systemProperties.wsURL}")
    private String WS_URL = "http://example.vn/services";
     
    @Value(value = "#{systemProperties.wsUsername}")
    private String WS_USER_NAME = "username";
     
    @Value(value = "#{systemProperties.wsPassword}")
    private String WS_PASSWORD = "password";
```

### **BP506: Use enums instead of integer constants**

**Problem**: Integer constants are used to represent object’s internal states

**Type**: Maintainability

**Severity**: Serious

**Bad code / behavior**: 

```java
private static final int EVENT_REF_DATA = 1;
private static final int ALL_RATE_TYPE_REF_DATA = 2;
private static final int RATE_TYPE_REF_DATA = 3;
private static final int SERVICE_REF_DATA = 4;
private static final int ALL_SERVICE_ITEM_REF_DATA = 5;
private static final int SERVICE_ITEM_REF_DATA = 6;
private static final int ALL_PERFORMANCE_REF_DATA = 7;
private static final int PERFORMANCE_REF_DATA = 8;
```

**Description**: 

Whenever you find yourself writing the above code to represent internal states or flags of a module, consider using enums instead. Enums are a better approach than integer constants because they are forced to fit within the range of allowed values, whereas using an int variable can contain the whole range of integers even if only a few of them are valid states.

Also, enums ease debugging and logging because you can print the human readable value by directly invoking the method toString(), while when using integers you would have to write a special purpose function to provide this functionality.

**Good code / behavior**: 

```java
public enum ServiceReferenceEnum {
    EVENT_REF_DATA,
    ALL_RATE_TYPE_REF_DATA,
    RATE_TYPE_REF_DATA,
    SERVICE_REF_DATA,
    ALL_SERVICE_ITEM_REF_DATA,
    SERVICE_ITEM_REF_DATA,
    ALL_PERFORMANCE_REF_DATA,
    PERFORMANCE_REF_DATA;
}
```

# **Transaction management**

### **BP601: Statements modifying database structure (DDL) are auto-committed**

**Problem**: Statements modifying database structure (DDL) are auto-committed

**Type**: Reliability

**Severity**: Critical

**Bad code / behavior**: 

**Service code:**

```java
@Service
@Scope(BeanDefinition.SCOPE_SINGLETON)
@Transactional
public class EmployeeServiceImpl implements EmployeeService {
 
    @Override
    @Transactional(rollbackFor = { EmployeeNameExistedException.class, DepartmentFullException.class })
    public void importEmployees(List<Employee> employees) throws EmployeeNameExistedException, DepartmentFullException {
        disableDatabaseIndexes();
        for (Employee employee : employees) {
            save(employee);
        }
        enableDatabaseIndexes();
    }
```

**Batch code:**

```java
@Component
public class EmployeeImportBatch {
 
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeImportBatch.class);
 
    @Inject
    private EmployeeService employeeService;
 
    public void execute() {
        List<Employee> employees = prepareData();
        try {
            employeeService.importEmployees(employees);
        } catch (EmployeeNameExistedException e) {
            LOGGER.error("Employee name {0} existed", e.getViolatedName());
        } catch (DepartmentFullException e) {
            LOGGER.error("Department {0} is full(max employees alowed is {1})", e.getDepartmentName(),
                    e.getMaxEmployee());
        }
    }
```

**Description**: The configuration to rollback transaction in case of errors in the example above will **not** work as the transaction itself has been already committed before when either disableDatabaseIndexes() or enableDatabaseIndexes() which contains DDL statements is called.

**Good code / behavior**: 

**Service code:**

```java
@Service
@Scope(BeanDefinition.SCOPE_SINGLETON)
@Transactional
public class EmployeeServiceImpl implements EmployeeService {
 
    @Override
    @Transactional(rollbackFor = { EmployeeNameExistedException.class, DepartmentFullException.class })
    public void importEmployees(List<Employee> employees) throws EmployeeNameExistedException, DepartmentFullException {
        for (Employee employee : employees) {
            save(employee);
        }
    }
```

**Batch code:**

```java
@Component
public class EmployeeImportBatch {
 
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeImportBatch.class);
 
    @Inject
    private EmployeeService employeeService;
 
    public void execute() {
        List<Employee> employees = prepareData();
        try {
            disableDatabaseIndexes();
            employeeService.importEmployees(employees);
            enableDatabaseIndexes();
        } catch (EmployeeNameExistedException e) {
            LOGGER.error("Employee name {0} existed", e.getViolatedName());
        } catch (DepartmentFullException e) {
            LOGGER.error("Department {0} is full(max employees alowed is {1})", e.getDepartmentName(),
                    e.getMaxEmployee());
        }
    }
```

**Comment**: Using DDL in a program is not really a good practice! Try to avoid this.

### **BP602: Single unit-of-work (use-case) should be enclosed inside one database transaction top reserve data integrity**

**Problem**: Splitting single unit-of-work(use-case) into multiple database transactions can easily lead to data integrity violation

**Type**: Reliability

**Severity**: Critical

**Bad code / behavior**: 

**Presentation code:**

```java
@Controller
public class EmployeeController {
 
    @Inject
    private EmployeeService employeeService;
 
    @Inject
    private DepartmentService departmentService;
 
    public void promoteToManager(Employee employee, Department department) {
				department.setManager(employee);
        departmentService.save(deparment);
        employee.setRole(RoleEnum.Manager);
        employeeService.save(employee);
    }
 }
```

**Service code:**

```java
@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {
 
    @Inject
    private EmployeeDao employeeDao;
 
    public void save(Employee object) {
        employeeDao.save(object);
    }
}
 
@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {
 
    @Inject
    private DepartmentDao departmentDao;
 
    public void save(Department object) {
        departmentDao.save(object);
    }
}
```

**Description**: 

Because `EmployeeServiceImpl` and `DepartmentServiceImpl` are configured to be transactional (every call from outside is wrapped inside a dedicated transaction), the single use case `promoteToManager(…)` is split into 2 database transactions; hence, might leave database in an inconsistent state if error occurs within `departmentService.save(…)`. Consequently, there will be a department whose manager is NOT yet a manager in such case.

Good approach is to encapsulate all database modifications derived from single unit-of-work (use-case) into one dedicated database transaction as example below. With this, the whole `promoteToManager(…)` method can either fully succeed or fail without leaving any inconsistency in database, thanks to the all-or-nothing “*atomicity*” of database transaction.

**Good code / behavior**: 

**Presentation code:**

```java
@Controller
public class EmployeeController {
 
    @Inject
    private EmployeeService employeeService;
 
    public void promoteToManager(Employee employee, Department department) {
        employeeService.promoteToManager(employee, department);
    }
    
}
```

**Service code:**

```java
@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {
 
    @Inject
    private EmployeeDao employeeDao;
 
    @Inject
    private DepartmentDao departmentDao;
 
 
    public void promoteToManager(Employee employee, Department department) {
				department.setManager(employee);
        departmentDao.save(deparment);
        employee.setRole(RoleEnum.Manager);
        employeeDao.save(employee);
    }
}
```

**Comment**: 

It is recommended to:

- have transaction demarcation (enclosing of sequential actions in transactional behavior) at service layer because it should be driven by business logic
- have transaction demarcation declared at class level(instead of method) with propagation mode set to REQUIRED (support a current transaction, create a new one if none exists) for better control over transactional behavior and maintainability

In case another propagation mode is required:

- create a dedicated technical service without any business logic inside
- strongly emphasize the technical purpose in its name (i.e. AlwaysExecuteInNewTransactionService)
- delegate all the work to “*real*” business services
- follow the second recommendation above to configure it with desired propagation mode accordingly

# **Performance**

### **BP701: Limit remote invocations between application layers**

**Problem**: Too many remote invocations between application layers

**Type**: Efficiency

**Severity**: Serious

**Bad code / behavior**: 

```java
public interface RegionalDataWebservice {
 
    List<Region> getAllRegions();
 
    List<Country> getAllCountries();
 
    List<Location> getAllLocations();
 
    List<Department> getAllDepartments();
}
 
 
@Component
public class EmployeeDetailModel {
 
    /**
     * Remote webservice that provides all regional data.
     */
    @Inject
    private RegionalDataWebservice regionalDataWebservice;
 
    private List<Region> allRegions;
    private List<Country> allCountries;
    private List<Location> allLocations;
    private List<Department> allDepartments;
 
    public EmployeeDetailModel() {
        // Initializes all reference data.
        allRegions = regionalDataWebservice.getAllRegions();
        allCountries = regionalDataWebservice.getAllCountries();
        allLocations = regionalDataWebservice.getAllLocations();
        allDepartments = regionalDataWebservice.getAllDepartments();
    }
}
```

**Description**: When working with remote interfaces, every call is expensive. As a result, we need to reduce the number of calls, and that means to transfer more data within each call. The solution is to create DTO(s) that can hold all data for the call. It needs to be serializable to go across the connection. Usually an assembling mechanism is used on the server side to convert data between the DTO(s) and domain objects.

**Good code / behavior**: 

```java
@Component
public class EmployeeDetailModel {
 
    /**
     * Remote webservice that provides all regional data.
     */
    @Inject
    private RegionalDataWebservice regionalDataWebservice;
 
    private RegionalDataDto regionalData;
 
    public EmployeeDetailModel() {
        // Initializes all reference data.
        regionalData = regionalDataWebservice.getRegionalReferenceData();
    }
}
 
 
@WebService
public class RegionalDataWebserviceImpl implements RegionalDataWebservice {
 
    @Inject
    private RegionDao regionDao;
    @Inject
    private CountryDao countryDao;
    @Inject
    private LocationDao locationDao;
    @Inject
    private DepartmentDao departmentDao;
     
    @Override
    @WebMethod
    public RegionalDataDto getRegionalReferenceData() {
        RegionalDataDto result = new RegionalDataDto();
        result.setAllRegions(regionDao.getAll());
        result.setAllCountries(countryDao.getAll());
        result.setAllLocations(locationDao.getAll());
        result.setAllDepartments(departmentDao.getAll());
        return result;
    }
}
 
public class RegionalDataDto implements Serializable {
 
    private List<Region> allRegions = new ArrayList<Region>();
    private List<Country> allCountries = new ArrayList<Country>();
    private List<Location> allLocations = new ArrayList<Location>();
    private List<Department> allDepartments = new ArrayList<Department>();
 
    public List<Region> getAllRegions() {
        return allRegions;
    }
    public void setAllRegions(List<Region> allRegions) {
        this.allRegions = allRegions;
    }
 
    public List<Country> getAllCountries() {
        return allCountries;
    }
    public void setAllCountries(List<Country> allCountries) {
        this.allCountries = allCountries;
    }
 
    public List<Location> getAllLocations() {
        return allLocations;
    }
    public void setAllLocations(List<Location> allLocations) {
        this.allLocations = allLocations;
    }
 
    public List<Department> getAllDepartments() {
        return allDepartments;
    }
    public void setAllDepartments(List<Department> allDepartments) {
        this.allDepartments = allDepartments;
    }
}
```

### **BP702: Beware the performance of string concatenation**

**Problem**: String concatenation inside the loop performs horribly if the number of items is large

**Type**: Efficiency

**Severity**: Non critical

**Bad code / behavior**: 

```java
@Override
public void exportToCSV(String filePath) {
    List<Employee> employees = employeeDao.getAll();
    String content = "";
    for (Employee employee : employees) {
        content += employee.asCSVFormat();
        content += LINE_SEPARATOR;
    }
    FileWriter fw = null;
    try {
        fw = new FileWriter(new File(filePath));
        fw.write(content.toString());
        fw.flush();
    } catch (IOException e) {
        LOGGER.error("Error occured when writing file", e);
        throw new RuntimeException("Error occured when writing file", e);
    } finally {
        if (fw != null) {
            try {
                fw.close();
            } catch (IOException e) {
                LOGGER.error("Error occured when closing file", e);
            }
        }
    }
}
```

**Description**: 

Operation on `String` (immutable) objects creates a lot of short-live objects. As consequence, using the string concatenation operator repeatedly to concatenate ***n*** strings requires time quadratic in ***n***.

To achieve acceptable performance, use a `StringBuilder` in place of String to store the under-construction string.

Improve the performance when concatenate string with `StringBuilder` just make sense in case we use it in the right context:

- Concatenate string inside the loop with large items.

Otherwise simply concatenate with + operator will make the coding more easier & convenience because at the compiler time it will be generated by using the `StringBuilder` (in Java) or `String.Concat` (in.Net) and there’ll not too much different in term of performance.

**Good code / behavior**: 

```java
@Override
public void exportToCSV(String filePath) {
    List<Employee> employees = employeeDao.getAll();
    StringBuilder content = new StringBuilder();
    for (Employee employee : employees) {
        content.append(employee.asCSVFormat());
        content.append(LINE_SEPARATOR);
    }
    FileWriter fw = null;
    try {
        fw = new FileWriter(new File(filePath));
        fw.write(content.toString());
        fw.flush();
    } catch (IOException e) {
        LOGGER.error("Error occured when writing file", e);
        throw new RuntimeException("Error occured when writing file", e);
    } finally {
        if (fw != null) {
            try {
                fw.close();
            } catch (IOException e) {
                LOGGER.error("Error occured when closing file", e);
            }
        }
    }
}
```

# **Security**

### **BP801: Avoid SQL Injection by using parameterized queries**

**Problem**: SQL injection

**Type**: Functionality

**Severity**: Critical

**Bad code / behavior**: 

```java
public class AuthenticationManagerImpl extends HibernateDaoSupport implements AuthenticationManager {
 
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordCredentials upc = UsernamePasswordCredentials.class.cast(authentication);
        String sql = "select 1 from user where username='" + upc.getUserName() + "' and password='"
                + upc.getPassword() + "'";
        SQLQuery query = getSession().createSQLQuery(sql);
        List result = query.list();
        if (result.isEmpty()) {
            throw new BadCredentialsException("Invalid username or password");
        }
        return authentication;
    }
}
```

**Description**: 

SQL injection is a [code injection](http://en.wikipedia.org/wiki/Code_injection) technique that exploits a [security vulnerability](http://en.wikipedia.org/wiki/Security_vulnerability) occurring in the [database](http://en.wikipedia.org/wiki/Database) layer of an [application](http://en.wikipedia.org/wiki/Application_software). The vulnerability is present when user input is either incorrectly filtered for [string literal](http://en.wikipedia.org/wiki/String_literal) [escape characters](http://en.wikipedia.org/wiki/Escape_sequences) embedded in [SQL](http://en.wikipedia.org/wiki/SQL) statements or user input is not [strongly typed](http://en.wikipedia.org/wiki/Strongly-typed_programming_language) and thereby unexpectedly executed.

In the above example, it is possible for attackers to provide a username containing SQL meta-characters that subvert the intended function of the SQL statement. For instance, by providing a username of

```sql
admin' OR '1'='1’--
```

and a blank password, the generated SQL statement becomes:

```sql
select * from user where username='admin' OR '1'='1'--' and password=''
```

This allows attackers to log in to the site without supplying a password, since the ‘OR’ expression is always true. Using the same technique attackers can inject other SQL commands which could extract, modify or delete data within the database.

Thus, it is recommended to always use parameterized queries so that variables passed as arguments to the queries will automatically be escaped by the database connection driver.

**Good code / behavior**: 

```java
public class AuthenticationManagerImpl extends HibernateDaoSupport implements AuthenticationManager {
 
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordCredentials upc = UsernamePasswordCredentials.class.cast(authentication);
        String sql = "select 1 from user where username = :username and password = :password";
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setString("username", upc.getUserName());
        query.setString("password", upc.getPassword());
        List result = query.list();
        if (result.isEmpty()) {
            throw new BadCredentialsException("Invalid username or password");
        }
        return authentication;
    }
}
```

### **BP802: Do not build HTML by concatenating strings without data escape.**

**Problem**: HTML elements are dynamically built by string concatenation in JavaScript

**Type**: Reliability

**Severity**: Critical

**Bad code / behavior**: 

```java
rhtml = '<div class="refinementBlock">';
rhtml += '<div class="ms-searchref-categoryname">';
rhtml += '<span class="tip" rel="w" title="';
rhtml += refData.ColumnDisplayName + '">';
rhtml += refData.ColumnDisplayName;
rhtml += '</span></div>';
rhtml += '<ul class="ms-searchref-filters">';
for (var k = 0; k < refData.Children.length; k++) {
    var tooltip = getRefinementTooltip(refData.Children[k]);
    rhtml += '<li class="ms-searchref-filter">';
    rhtml += '<a class="ms-searchref-filterlink tip" rel="w" href="';
    rhtml += refData.Children[k].Url;
    rhtml += '" title="' + tooltip + '" termid="';
    rhtml += refData.Children[k].TermId;
    rhtml += '" field="' + refData.Children[k].RefinementField;
    rhtml += '" token="' + refData.Children[k].RefinementToken + '">';
    rhtml += '<span>' + refData.Children[k].DisplayName + '</span>';
    rhtml += '<span class="ms-searchref-count"> (';
    rhtml += refData.Children[k].TotalCount + ')</span>';
    rhtml += '</a>';
    rhtml += '</li>';
}
rhtml += '</ul>';
rhtml += '</div>';
```

**Description**: 

There are multiple issues with the code above:

- If the `refData[j].Children[k].Url` or `refData[j].Children[k].DisplayName` contains the single quote `‘`, the code is likely to break
- The `refData[j].Children[k].Url` can be injected with malicious code and the page is vulnerable to XSS attacks.
- The code can grow complex easily and it’s difficult to maintain later.

Therefore, it is recommended that:

- Always escape the inputs before using them for building HTML
- Templating frameworks should be used for dynamically constructing HTML elements on client side in most of the cases.

**Good code / behavior**: 

Example with jQuery template

```java
<div id="parentId">
    <b>Refinements</b>
</div>
<script type="text/x-jquery-tmpl" id="RefinementTmpl">
    <div class="refinementBlock">
        <div class="ms-searchref-categoryname">
            <span class="tip" rel="w" title="${ColumnDisplayName}">
        ${ColumnDisplayName}
    </span>
        </div>
        <ul class="ms-searchref-filters">
    {{tmpl(Children) "#RefinementItemTmpl"}}
    </ul>
    </div>
</script>
<script type="text/x-jquery-tmpl" id="RefinementItemTmpl">
    <li class="ms-searchref-filter">
        <a  class="ms-searchref-filterlink tip" rel="w"
            href="${Url}"
            title="${Tooltip}"
            termid="${TermId}"
            field="${RefinementField}"
            token="${RefinementToken}">
            <span>${DisplayName}</span>
            <span class="ms-searchref-count">${Count}</span>
        </a>
    </li>
</script>
<script>
    var data = EscapseUnsafeData(refData);
    $("#RefinementTmpl").tmpl(data).appendTo("#parentId");
</script>
```

### **BP803: Fail safe coding**

**Problem**: Whenever a failure occurs in the system, it were put into an unsafe situation due to **insufficient** coding logic.

**Type**: Reliability

**Severity**: Critical

**Bad code / behavior**: 

![image](https://github.com/user-attachments/assets/d90c2b13-bc82-4d0a-bb0a-6dfd19d0fd17)

![image 1](https://github.com/user-attachments/assets/daea4780-8bac-4768-a184-cdf97b94e0a5)

![image 2](https://github.com/user-attachments/assets/eb426205-7c9a-48cd-83e0-b59faca1142f)

**Description**: 

Failure happen in our program is something that we cannot prevent at all even if we are put awareness while coding. Pay attention on error handling is crucial but it is more important than that is what happen to our program after the error occurs. The following questions should come into our mind in this situation:

- Should the system continue to operate? (fail-close, fail-open)
- Do system still under safe/sufficient situation?
- Does it easy for the system administrator trace back the error and finding the root cause?

Taking the definition of Fail-safe principle ([https://en.wikipedia.org/wiki/Fail-safe](https://en.wikipedia.org/wiki/Fail-safe)), it is crucial for us as a developer take into account it while coding the application logic. By that, hopefully we can put the application in a more safer situation.

A fail-safe in engineering is a design feature or practice that in the event of a specific type of failure, inherently responds in a way that will cause no or minimal harm to other equipment, the environment or to people.

Unlike inherent safety to a particular hazard, a system being "fail-safe" does not mean that failure is impossible or improbable, but rather that the system's design prevents or mitigates unsafe consequences of the system's failure. That is, if and when a "fail-safe" system "fails", it is "safe" or at least no less safe than when it was operating correctly.

**Good code / behavior**: 

![image 3](https://github.com/user-attachments/assets/7ea4d7c5-4981-4a8f-9172-1ee9f4a6072c)

![image 4](https://github.com/user-attachments/assets/81cd3534-6da6-481f-b583-98fae7369f69)

![image 5](https://github.com/user-attachments/assets/343e2de8-3653-45a0-a4b7-6304af10149b)

**Comment**: 

Since many types of failure are possible, failure mode and effects analysis is used to examine failure situations and recommend safety design and procedures.

Two of the most common fail-safe operations include "fail-open" and "fail-close". In a fail-open situation, a signal loss or failure of the system causes the valve to completely open. This causes the fluid flowing through the pipe to continuously pass through. In a fail-close situation, a signal loss causes the valve to close completely. This causes the fluid flowing through the pipe to be completely prevented from flowing any further.

### **BP804: Robust data input**

**Problem**: Lacking of data input checking may put system under in-consistence business state or even worse is that it being attacked, leaking data.

**Type**: Reliability

**Severity**: Critical

**Bad code / behavior**: 

![image 6](https://github.com/user-attachments/assets/333e6776-a28f-4596-b2d2-d67a9d821734)

![image 7](https://github.com/user-attachments/assets/792ed58e-7dfc-438d-8d04-5e91e6969209)

==> Those input are going to store or used in system directly

**Description**: 

Building a system with lacking of data input checking can lead to those scenarios:

- System data integrity is violated. (e.g: Lacking of business validation rule based on data input)
- System being attacked by injection. (e.g: XSS attack)
- Data lost/leaking to unauthorized people. (e.g: SQL injection)
- And many more …

**IMPORTANT:** Input validation has to do on both sides. Validation on front-end is just for user convenient. Validation on back-end is crucial for application, never trust the data received from front-end.

**Good code / behavior**: 

Following diagram is a good illustration for the system that apply the data input checking properly by aware and put the check into all possible scenario in sequence from front-end/client to back-end/server.


![image 8](https://github.com/user-attachments/assets/5a5c13f6-3548-4dda-b609-b0f181a7d583)

There’s one more element that we also need to consider to ensure the robust data input is that at the database level where all column type definition, length, constraint need to be consider and setup correctly based on the specification and consequence is that it required the entity model defined in ORM (if have) need also be define correctly.

### **BP805: Robust data output**

**Problem**: Attackers do not always target the server. Sometimes they use it **as a relay** to target clients

**Type**: Reliability

**Severity**: Critical

**Bad code / behavior**: 

```java
// jstl core
<c:out value="${review.body}" />
// jstl functions
<div>${review.body}</div>
// jsf
<h:outputText value="#{review.body}"/>
// 3rd- party API - Apache Commons
<div><%=review.body%></div>
// 3rd. Party API - OWASP ESAPI
<div><%=review.body%></div>
// C#
<div>@review.body</div>
```

==>  The attacker can use "hijacking" clicks meant for their page and routing them to another page, most likely owned by another application, domain, or both.

**Description**: 

If we do not encode data when displaying them back to the user, it will create a high risk on the application which attacker can use click-jacking attacks to trick a user into clicking on a button or link on another page when they were intending to click on the the top level page.

For example, imagine an attacker who builds a web site that has a button on it that says "click here for a free iPod". However, on top of that web page, the attacker has loaded an iframe with your mail account, and lined up exactly the "delete all messages" button directly on top of the "free iPod" button. The victim tries to click on the "free iPod" button but instead actually clicked on the invisible "delete all messages" button. In essence, the attacker has "hijacked" the user's click, hence the name "Clickjacking".

**Good code / behavior**: 

Encode data when displaying them back to the user, not when storing them

For example, using library encoder

```java
// jstl core
<c:out value="${review.body}" escapeXml="true" />
// jstl functions
<div>${fn:escapeXml(review.body)}</div>
// jsf
<h:outputText value="#{review.body}" escape="true" />
// 3rd- party API - Apache Commons
<div><%=StringEscapeUtils.escapeHtml(review.body)%></div>
// 3rd. Party API - OWASP ESAPI
<div><%=ESAPI.encoder().encodeForHTML(review.body)%></div>
// C#
<div>@<AntiXss.HtmlAttributeEncode(review.body)</div>
```

Encode according to the data destination, e.g encodeForHtml, encodeForJSON, encodeForXml, encodeForUrl, encodeForHtmlAttribute,…

Defending against Clickjacking with these ways:

- Sending the proper X-Frame-Options HTTP response headers that instruct the browser to not allow framing from other domains, i.e. **`response.addHeader(“X-FRAME-OPTIONS”, “DENY”);`**
- Employing defensive code in the UI to ensure that the current frame is the most top level window

### **BP806: Triple A**

**Problem**: 

**Triple A (Authentication, authorization, and accounting - AAA)** is stand for is a term for a framework for intelligently controlling access to computer resources, enforcing policies, auditing usage, and providing the information necessary to bill for services. These combined processes are considered important for effective network management and security.

- **Authentication** provides a way of identifying a user, typically by having the user enter a valid user name and valid password before access is granted
- **Authorization** is the process of enforcing policies: determining what types or qualities of activities, resources, or services a user is permitted.
- **Accounting** measures the resources a user consumes during access. This can include the amount of system time or the amount of data a user has sent and/or received during a session. Accounting is carried out by logging of session statistics and usage information and is used for authorization control, billing, trend analysis, resource utilization, and capacity planning activities.

**Type**: Reliability

**Severity**: Critical

**Bad code / behavior**: 

![image 9](https://github.com/user-attachments/assets/d2511b59-0979-40c3-8706-d1aea91b9304)

![image 10](https://github.com/user-attachments/assets/13f73ef5-6364-443a-8061-9d5963fa7872)

![image 11](https://github.com/user-attachments/assets/a03d7f4f-9c7b-4817-99e5-9b266268b7bd)

**Good code / behavior**: 

Any routines accessing data or calling services should ensure that:

- the user is authenticated by the service (not at the client-side!!)
- the user is authorized by the service
- the event is audited by the service

Discretionary access control: access control in the SQL/Application logic

Access control list can get very complicated and may need to be designed entirely within the application! This necessitates audit trails

- Administrative / security management
- Move the administrative interface to a separate port
- Restrict access to authorized networks only
- Avoid to use default admin accounts
- Avoid to choose weak passwords
- Set a policy for Account lockout
- Separation of Duties and avoid “God” access

Secret URLs

- Each page must perform an ACL check to ensure that the user is authorized access

Security design principle “Deny all by default”

- Application-layer
    - Should not run as root/admin
    - Should be restricted to specific working folders, disallowing all access outside of the folder
    - No default accounts/passwords
    - No default privileges
    - No insecure default settings
- Service-layer
    - Should not run as root/admin
    - Server user should not have any unnecessary permissions
    - All unnecessary services disabled, all remaining services hardened.
- Network-layer
    - Deny all inbound/outbound traffic by default

Audit trails:

- Error / Troubleshooting trail:
- Detailed error messages, stack traces, etc.
- Usually may contain the error reference (that was showed to the user)
- Audience: developer, sysadmin

Audit/Compliance trail:

- All data modifications, all authentication events, etc.
- Audience: compliance/regulator

Monitoring trail:

- All user actions
- Audience: behavior analysis, intrusion detection, etc.

### **BP807: Parameterized access**

**Problem**: Parameterized access

**Type**: Reliability

**Severity**: Critical

**Bad code / behavior**: 

**JPA / HQL hibernate (NHibernate)**

```java
List results = entityManager.createNativeQuery("Select * from Books where author = " + author).getResultList();
int resultCode = entityManager.createNativeQuery("Delete from Cart where itemId = " + itemId).executeUpdate();
```

**Stored procedures**

```java
CREATE PROCEDURE SP_save_referrer @refurl varchar(1000) = NULL AS
DECLARE @sql nvarchar(4000)
INSERT @sql = 'INSERT INTO referrers_history (url,when)
  VALUES ('+ @refurl + ','+ NOW() + ')'
EXEC (@sql)
```

These code is very bad because the content of the parameter will modify the overall query (SQL injection).

**Description**: 

It must be awared that:

- Command parameterization turns a parameter into a parameter at the protocol layer!
- JPA / Native SQL / HQL (hibernate/NHibernate) / Stored procedures is vulnerable to injections
- Input validation prevents hostile/corrupt data from entering the system but it does not prevent injections.
- NoSQL databases are also vulnerable to injections

**Good code / behavior**: 

Use Parameterized access in Hibernate/Nhibernate/ Entity framework

Parameterized access to external resources checklist:

- Check if parameterized access is possible.
- If not possible, use encoding (see chapter: data output), else if not possible, use escaping O’Brian -> O\’Brian

# **ORM**

### **BP901: Avoid N + 1 selects**

**Problem**: N + 1 selects

**Type**: Efficiency

**Severity**: Critical

**Bad code / behavior**: 

**Hibernate mapping:**

```java
@Entity
public class Department {
 
    private long departmentId;
    private String departmentName;
    private Set<Employee> employees = new HashSet<Employee>();
 
    @Id
    public long getDepartmentId() {
        return departmentId;
    }
 
    public void setDepartmentId(long departmentId) {
        this.departmentId = departmentId;
    }
 
    @NotNull
    public String getDepartmentName() {
        return departmentName;
    }
 
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
 
    @OneToMany
    public Set<Employee> getEmployees() {
        return employees;
    }
 
    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }
}
```

**Service-layer code:**

```java
@Service
public class DepartmentServiceImpl implements DepartmentService {
     
    @Inject
    private DepartmentDao departmentDao;
 
    @Override
    public List<Department> findNoneEmptyDepartments() {
        List<Department> results = new ArrayList<Department>();
        DetachedCriteria criteria = DetachedCriteria.forClass(Department.class);
        List<Department> allDepartments = departmentDao.findByCriteria(criteria);
        for (Department department : allDepartments) {
            if (!department.getEmployees().isEmpty()) {
                results.add(department);
            }
        }
        return results;
    }
}
```

**Generated SQL:**

```sql
/* First query to get all departments*/
SELECT *
  FROM departments;
 
/* For each call to department.getEmployees().isEmpty() */
SELECT *
  FROM employees
 WHERE department_id = ?;
```

**Description**: 

As Department has a lazy OneToMany relationship to Employee, every access on employees attribute of Department results in a SQL query to populate the corresponding list.
Suppose that N departments are return by the first query, there will be in total (N + 1) queries generated by this code, including:

- 1 query to get all departments
- N queries to get all employees for each department
This always has a very bad impact on performance as too many queries and roundtrips between application server and database are generated.

**Good code / behavior**: 

**1st option: Early-fetching all necessary associations:**

**JAVA CODE:**

```java
@Override
public List<Department> findNoneEmptyDepartments() {
    List<Department> results = new ArrayList<Department>();
    DetachedCriteria criteria = DetachedCriteria.forClass(Department.class)
            .setFetchMode("employees",FetchMode.JOIN);
    List<Department> allDepartments = departmentDao.findByCriteria(criteria);
    for (Department department : allDepartments) {
        if (!department.getEmployees().isEmpty()) {
            results.add(department);
        }
    }
    return results;
}
```

**Generated SQL:**

```sql
SELECT *
  FROM departments dept LEFT OUTER JOIN employees empl
       ON dept.department_id = empl.department_id;
```

**2nd option: Moving server operation to database (if applicable):**

**JAVA CODE:**

```sql
@Override
public List<Department> findNoneEmptyDepartments() {
    DetachedCriteria criteria = DetachedCriteria.forClass(Department.class)
            .add(Restrictions.isNotEmpty("employees"));
    return departmentDao.findByCriteria(criteria);
}
```

**SQL STATEMENT:**

```sql
SELECT *
FROM departments dept
WHERE EXISTS (SELECT 1
                 FROM employees empl
                WHERE dept.department_id = empl.department_id);
```

### **BP902: Load only necessary data from database**

**Problem**: Loading the whole information from database and then, applying filter in memory can cause very poor performance with big data

**Type**: Efficiency

**Severity**: Critical

**Bad code / behavior**: 

**SHAREPOINT CODE:**

```java
private string GetEmployeeVisaById(int id) {
        using (SPWeb empWeb = SPContext.Current.Site.Open("/employee")) {
            SPList employeeList = empWeb.Lists["Employees"];
            SPListItem empItem = employeeList.Items.GetItemById(id);
            return empItem["Visa"] as string;
        }
}
```

**Description**: 

SPList.Items.GetItemByiddoes actually 2 steps:

- `Loading the entire SPList.Items collection into memory`
- `Filtering the previously loaded list by ID`

This always causes poor performance for big collection of SPList.Itemsas there are too much data loaded from database and sent over the network to application server.

**Good code / behavior**: 

**Using the right API to apply filtering on database so that only the necessary data is loaded and sent over network**

**SHAREPOINT CODE:**

```java
private string GetEmployeeVisaById(int id) {
        using (SPWeb empWeb = SPContext.Current.Site.Open("/employee")) {
            SPList employeeList = empWeb.Lists["Employees"];
            SPListItem empItem = employeeList.GetItemById(id);
            return empItem["Visa"] as string;
        }
}
```

**Comment**: 

In other ORM (i.e. Hibernate / NHibernate / EntityFramework), there are typically 2 techniques used to limit the data queried from database:

- Apply projection to load only required columns from database (instead of the whole entity) => This is recommended for search screens as the number of displayable columns are usually much more smaller than the number of those in database.
- Apply pagination to load only a limited number of rows from database (instead of all matching rows) => This is recommended for:
    - first matching row search as only the first row is accessed by database and then, sent to application server
    - pagable screens as loading the whole data and then, paging in memory is rarely a good solution in term of performance
    - batch processing as loading a huge amount of data can slow down or even kill the application server

### **BP903: Correct concurrent update handling**

**Problem**: Incorrect concurrent update handling with ORM (Hibernate)

**Type**: Reliability

**Severity**: Serious

**Bad code / behavior**: 

```java
// Dto class
@Getter @Setter
public class TextTranslateDto {
    private long id;
    private int version;
    private String enText;
    private String deText;
    private String frText;
    private String itText;
}
     
// Controller class
@Slf4j
@RestController
@RequestMapping(AbstractServiceEndpoint.PRIVATE_ADMIN_PATH)
public class AdminServiceEndpoint {
     
    @Autowired
    private PgrelCommonMapper commonMapper;
     
    @Autowired
    private IAdminService adminService;
     
    @PostMapping("text")
    public TextTranslateDto updateText(@RequestBody TextTranslateDto textTranslateDto) {
        // Hibernate session scope (EntityManagerFactory.createEntityManager())
        TextTranslationEntity textTranslation =
    adminService.getTextTranslation(textTranslateDto.getId());
         
        textTranslation.setVersion( textTranslateDto.getVersion() );
        textTranslation.setEnText( textTranslateDto.getEnText() );
        textTranslation.setDeText( textTranslateDto.getDeText() );
        textTranslation.setFrText( textTranslateDto.getFrText() );
        textTranslation.setItText( textTranslateDto.getItText() );
         
        return commonMapper.toTextTranslateDto(
    adminService.updateTextTranslation(textTranslation));
        // Hibernate session scope (EntityManagerFactory.close())
    }
}
 
// Service class
@Service
@Transactional(rollbackFor = Throwable.class)
public class AdminServiceImpl implements IAdminService {
     
    @Autowired
    private ITextTranslationRepository textTranslationRepo;
     
    @Override
    public TextTranslationEntity updateTextTranslation(
            TextTranslationEntity translationEntity) {
        return textTranslationRepo.save(translationEntity);
    }
}
```

**Description**: 

Concurrent update handling is provided by ORM framework (e.g: using **@Version** or **@Timestamp** in Hibernate), however using it without knowing clearly in detail the working mechanism of the using framework may lead to unexpected behavior (in concrete case may lead to the mechanism not working).

In the example above the concurrent update handling is not working, means the later modification still win and override on the previous modification without any awareness from the user.

**Problem**: With Hibernate, perform the modification on field version/timestamp of the persistence entity will not cause any effect as the framework prevent it by default whenever the entity is managed by the Hibernate session. (In the above example the textTranslation is inside the Hibernate session)

Here’s the suggested pattern that can make the concurrent update mechanism working:

1. Transfer the id & version field together with related data need to be updated from server to client.
2. Perform the modification on client side.
3. Send back to server the modified data together with id & version field at step 1.
4. Load the persistence entity from service (with id field) and then make it detached. (either by move the entity out of the session scope or detach it manually).
5. Perform the modification with data send from step 3 **including the field version.**
6. Persist the detached entity back to Hibernate session.

**Good code / behavior**: 

```java
// Dto class
@Getter @Setter
public class TextTranslateDto {
    private long id;
    private int version;
    private String enText;
    private String deText;
    private String frText;
    private String itText;
}
     
// Controller class
@Slf4j
@RestController
@RequestMapping(AbstractServiceEndpoint.PRIVATE_ADMIN_PATH)
public class AdminServiceEndpoint {
     
    @Autowired
    private PgrelCommonMapper commonMapper;
     
    @Autowired
    private IAdminService adminService;
     
    @PostMapping("text")
    public TextTranslateDto updateText(@RequestBody TextTranslateDto textTranslateDto) {
        // Hibernate session scope (EntityManagerFactory.createEntityManager())
        TextTranslationEntity textTranslation =
    adminService.getTextTranslation(textTranslateDto.getId());
        // Hibernate session scope (EntityManagerFactory.close())
         
        // textTranslation is detached now
        // and changing on version field is take into account.
        textTranslation.setVersion( textTranslateDto.getVersion() );
        textTranslation.setEnText( textTranslateDto.getEnText() );
        textTranslation.setDeText( textTranslateDto.getDeText() );
        textTranslation.setFrText( textTranslateDto.getFrText() );
        textTranslation.setItText( textTranslateDto.getItText() );
         
        // Hibernate session scope (EntityManagerFactory.createEntityManager())
        return commonMapper.toTextTranslateDto(
    adminService.updateTextTranslation(textTranslation));
        // Hibernate session scope (EntityManagerFactory.close())
    }
}
 
// Service class
@Service
@Transactional(rollbackFor = Throwable.class)
public class AdminServiceImpl implements IAdminService {
     
    @Autowired
    private ITextTranslationRepository textTranslationRepo;
     
    @Override
    public TextTranslationEntity updateTextTranslation(
            TextTranslationEntity translationEntity) {
        return textTranslationRepo.save(translationEntity);
    }
}
```

**References:**

1. Joshua Bloch. *Effective Java – Second Edition*. Addison-Wesley, 2008
2. Generic Coding Best Practices. PHD, HNT, COH, DHN, 04.09.2019.