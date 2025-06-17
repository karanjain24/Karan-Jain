GoodBudget-KaranJain

Tool : Playwright
Coding Language : Java
Test Framework : JUnit
Design Pattern : Page Object Model

The framework is designed using Page Object Model pattern to segregate the page classes from tests using Playwright and Java.
All of the major entities like Home, Envelopes, Transactions, Accounts, Fill Envelopes, Reports etc have their page classes while E2EScenarios hold the test cases to be executed.
Base class has the setup and teardown methods as well as page initialization methods.
Utilities class has the common methods to be used.
JsonDataLoader class loads the data held in testdata.json file.
testdata.json file has a few basic details used by the test cases.
The framework also supports parallel cross browser execution, although its recommended to execute the suite in only one browser at once.
This is because the data is shared across application per user and hence 3 users are used to test the 3 scenarios in parallel.

There are three scenarios included to test the end to end business flows as well as carry out a few validations.

e2eScenario1 :
This tests the flow of adding initial funds to the Account, adding Income, adding New Envelopes, Filling Envelopes, adding Single and Multiple Transactions, checking Reports and all validations related to these events.

e2eScenario2:
This tests the flow of Overspending from the Envelopes, Transferring funds between envelopes, removing filled envelopes and related validations of the activities.

validationChecks:
This tests a few validations for adding New Envelopes, Overdraft, Scheduling Transactions and Overfilling Envelopes.

To avoid data corruption, resetApplication method runs to delete all the transactions as well as maintain the default Envelope status.

There are a lot of validations involving calculation of total spending, envelope spending, comparing transaction records, header messages etc.

The framework can be scaled easily to include many scenarios as well as validations.

Although, there is immense scope of optimization in the framework pertaining to synchronization and following SOLID and OOPS concepts even further.