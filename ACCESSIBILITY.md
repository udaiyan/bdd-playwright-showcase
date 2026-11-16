# Accessibility Statement

Accessibility status of the practice site (https://practice-automation.com)
as measured by this framework.

## Testing approach

Automated scanning with **axe-core** against **WCAG 2.2 AA** rule tags
(wcag2a, wcag2aa, wcag21a, wcag21aa, wcag22aa).

Automated testing catches only a fraction of accessibility issues (GDS
guidance puts it around 30%). This suite complements, never replaces, manual
testing with assistive technologies.

## Observed results (critical / serious)

Counts below are what the suite actually reported. Rule-level detail for
every violation is attached to each scenario as an `axe-violations` text
attachment in the Cucumber report.

| Page        | Critical | Serious |
|-------------|----------|---------|
| Home        | 0        | 1       |
| Form fields | 1        | 0       |
| Popups      | 0        | 1       |
| Modals      | 0        | 1       |
| Iframes     | 0        | 1       |
| Tables      | 0        | 1       |

Total: 1 critical, 5 serious. Moderate/minor counts are in the attached
reports rather than enumerated here.

## Findings from test development

These markup gaps were diagnosed while building locators, and are
corroborated by the axe scans:

- Modal titles are styled text, not semantic headings (no h1-h6, no
  role="heading"), so role-based heading queries find nothing.
- DataTables sortable column headers lack proper columnheader roles.
- The "Do you like automation?" select label is not reliably associated
  with its control.
- A hidden site-wide contact form duplicates visible labels, causing
  strict-mode ambiguity for substring label queries.

## Handling known issues in production

1. Fix in the codebase (preferred):

```java
AxeResults results = new AxeBuilder(page)
    .withTags(Arrays.asList("wcag2a", "wcag2aa", "wcag21a", "wcag21aa", "wcag22aa"))
    .analyze();

assertThat(results.getViolations())
    .as("WCAG 2.2 AA violations")
    .isEmpty();
```

2. Exclude / disable known third-party issues (temporary baseline):

```java
AxeResults results = new AxeBuilder(page)
    .withTags(Arrays.asList("wcag2a", "wcag2aa", "wcag21a", "wcag21aa", "wcag22aa"))
    .exclude("#third-party-widget")
    .disableRules(Arrays.asList("color-contrast"))
    .analyze();
```

3. Publish remaining issues in the service's accessibility statement with
   a resolution timeline and an alternative accessible route.

## Manual testing still required

- Screen reader navigation (NVDA, JAWS, VoiceOver)
- Keyboard-only operation
- Voice control
- Readability and cognitive load
- Colour-blindness and low-vision checks
- Motor-impairment input methods

## References

- GDS Service Manual: making your service accessible
- WCAG 2.2 specification
- axe-core rule descriptions
