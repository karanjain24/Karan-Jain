
1. Exploratory Testing Charters
   | Charter ID | Scope / Focus Area                   | Description                                                             | Priority |
   | ---------- | ------------------------------------ | ----------------------------------------------------------------------- | -------- |
   | C1         | User Signup and Login                | Test sign-up, login, logout flows, input validation, error handling     | High     |
   | C2         | Budget and Envelope Management       | Create/edit/delete envelopes, add initial funds, fund transfers         | High     |
   | C3         | Transactions                         | Add, edit, delete transactions, check multiple transaction types        | High     |
   | C4         | Reports and Visualizations           | Generate and view spending reports, charts, and summaries               | Medium   |
   | C5         | Responsive Design & Cross-browser UI | Verify UI behavior on various screen sizes and browsers                 | Medium   |
   | C6         | Performance & Accessibility          | Check page load speed, keyboard navigation, screen reader compatibility | Medium   |
   | C7         | Security & Privacy                   | Inspect cookies/localStorage, check HTTPS, data storage                 | High     |


2. Findings from Each Charter
C1: User Signup and Login
What worked:

Signup form accepted valid data.
Login and logout worked smoothly.
Forget Password worked properly.

Issues found:

Password Requirement not very strong.
No CAPTCHA or bot prevention on signup (potential spam risk).

C2: Budget and Envelope Management
What worked:

Envelopes can be created, funded, and deleted successfully.
Fund transfers between envelopes update balances immediately.
Deleting Filled Envelopes gives a warning message

Issues found:

No confirmation dialog before deleting an empty envelope.
Overfilling envelope allowed.
Filling of Envelopes without the available balance is allowed, thus creating negative Available Balance.

C3: Transactions
What worked:
Multiple transaction types (income, expense, transfers) can be added and edited.
Transactions can be exported easily
Scheduling can be done easily

Issues found:

Transactions can be done on zero available balance, although Header Message stating the same appears
Transactions with future dates can be added without warning.
Not all transaction records can be deleted using Multi check > Delete option

C4: Reports and Visualizations
What worked:

Reports load quickly and accurately reflect data.
Charts update dynamically with new transactions.

Issues found:

On smaller screens, some charts overflow their containers and become hard to read.

C5: Responsive Design & Cross-browser UI
What worked:

Layout adjusts well on desktop browsers (Chrome, Edge).

Issues found:

Firefox shows minor layout glitches. Texts not very sharp

C6: Performance & Accessibility
What worked:

Lighthouse score: performance ~77%, accessibility ~88%.

Keyboard navigation works for most interactive elements.

Issues found:

Missing ARIA labels on some buttons.
Text Input fields didn't have text associated with the locator/ element

C7: Network Analysis
What worked:

Most requests are carried out quickly without much wait time

Issues found:

Certain api calls, transactions, took a long time to be executed

3. Prioritization of Charters
   | Charter ID | Priority | Reasoning                                                          |
   | ---------- | -------- | ------------------------------------------------------------------ |
   | C1         | High     | User authentication is critical for any user-facing app.           |
   | C2         | High     | Core functionality around budgeting/envelopes impacts usability.   |
   | C7         | High     | Security and privacy are essential to protect user data.           |
   | C3         | High     | Transactions are the heart of budget tracking.                     |
   | C5         | Medium   | Responsive UI is important but less critical than core features.   |
   | C6         | Medium   | Accessibility affects usability for some users, needs improvement. |
   | C4         | Medium   | Reports are important but secondary to data integrity.             |

4. Risks and Mitigation Strategies
   | Risk                                                            | Mitigation                                                          |
   | --------------------------------------------------------------- | ------------------------------------------------------------------- |
   | User data leakage due to insecure storage                       | Encrypt sensitive data, use secure storage mechanisms.              |
   | Accidental data loss (e.g., deleting envelopes or transactions) | Add confirmation dialogs and undo options.                          |
   | Poor input validation leading to invalid or inconsistent data   | Enforce strict client and server-side validation.                   |
   | Inadequate accessibility reducing usability for disabled users  | Improve ARIA support, keyboard navigation, and focus indicators.    |
   | Cross-browser and device inconsistencies                        | Test thoroughly on popular browsers and device sizes.               |
   | Performance issues on slower devices                            | Optimize assets, lazy load content, and reduce unnecessary scripts. |
   | Security vulnerabilities (XSS, CSRF, etc.)                      | Implement CSP, secure cookies, CSRF tokens, and regular audits.     |
