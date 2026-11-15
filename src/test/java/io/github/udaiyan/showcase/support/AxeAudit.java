package io.github.udaiyan.showcase.support;

import com.deque.html.axecore.playwright.AxeBuilder;
import com.deque.html.axecore.results.AxeResults;
import com.deque.html.axecore.results.Node;
import com.deque.html.axecore.results.Rule;
import io.github.udaiyan.showcase.context.TestContext;

import java.util.Arrays;

/**
 * Scenario-scoped accessibility auditing via axe-core, shared by step classes
 * via PicoContainer.
 *
 * Runs axe with WCAG 2.2 AA rules and reports violations by impact severity.
 * Fails on critical/serious violations; logs moderate/minor as warnings.
 *
 * Known third-party violations (ads, embedded iframes, DataTables plugin)
 * can be excluded via baseline rules, mirroring how GOV.UK publishes "known
 * issues" in accessibility statements.
 *
 * Full violation details are attached to the Cucumber report on failure.
 *
 * Note: the Deque docs had incorrect package paths. Inspection of the published
 * 4.8.0 dequeutilites.jar shows the real structure:
 *   - AxeResults.getViolations() returns List<Rule>
 *   - Rule has getId(), getImpact(), getDescription(), getHelp(), getHelpUrl(), getNodes()
 *   - Rule.getNodes() returns List<Node>
 *   - Node has getTarget() and getHtml()
 */
public class AxeAudit {

    private final TestContext context;
    private AxeResults lastResult;

    public AxeAudit(TestContext context) {
        this.context = context;
    }

    /**
     * Runs axe-core with WCAG 2.2 AA rules against the current page.
     */
    public void scan() {
        lastResult = new AxeBuilder(context.page())
                .withTags(Arrays.asList(
                        "wcag2a", "wcag2aa", "wcag21a", "wcag21aa", "wcag22aa"
                ))
                .analyze();
    }

    public int violationCount() {
        return lastResult != null ? lastResult.getViolations().size() : 0;
    }

    public int criticalCount() {
        return countByImpact("critical");
    }

    public int seriousCount() {
        return countByImpact("serious");
    }

    private int countByImpact(String impact) {
        if (lastResult == null) return 0;
        return (int) lastResult.getViolations().stream()
                .filter(v -> impact.equals(v.getImpact()))
                .count();
    }

    /**
     * Summary of violations by impact severity.
     */
    public String summary() {
        if (lastResult == null) return "No audit run";
        long critical = lastResult.getViolations().stream().filter(v -> "critical".equals(v.getImpact())).count();
        long serious  = lastResult.getViolations().stream().filter(v -> "serious".equals(v.getImpact())).count();
        long moderate = lastResult.getViolations().stream().filter(v -> "moderate".equals(v.getImpact())).count();
        long minor    = lastResult.getViolations().stream().filter(v -> "minor".equals(v.getImpact())).count();
        return String.format("critical=%d, serious=%d, moderate=%d, minor=%d",
                critical, serious, moderate, minor);
    }

    /**
     * Full violation details as formatted text for Cucumber report attachment.
     *
     * Violations are typed as Rule (not a separate Violation class); each
     * Rule has getNodes() returning List<Node>.
     */
    public String fullReport() {
        if (lastResult == null) return "No audit run";
        StringBuilder sb = new StringBuilder();
        sb.append("Accessibility Audit Report\n");
        sb.append("==========================\n\n");
        sb.append("Violations: ").append(lastResult.getViolations().size()).append("\n\n");
        for (Rule v : lastResult.getViolations()) {
            sb.append("Rule: ").append(v.getId()).append("\n");
            sb.append("Impact: ").append(v.getImpact()).append("\n");
            sb.append("Description: ").append(v.getDescription()).append("\n");
            sb.append("Help: ").append(v.getHelp()).append("\n");
            sb.append("Help URL: ").append(v.getHelpUrl()).append("\n");
            sb.append("Nodes affected: ").append(v.getNodes().size()).append("\n");
            for (Node n : v.getNodes()) {
                sb.append("  - Target: ").append(n.getTarget()).append("\n");
                sb.append("    HTML: ").append(n.getHtml()).append("\n");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Attaches the full report to the Cucumber scenario on failure.
     */
    public void attachReportToScenario() {
        context.attachAxeReport(fullReport());
    }
}
