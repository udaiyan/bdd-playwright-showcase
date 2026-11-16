package io.github.udaiyan.showcase.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.github.udaiyan.showcase.config.FrameworkConfig;
import io.github.udaiyan.showcase.context.TestContext;
import io.github.udaiyan.showcase.pages.FormFieldsPage;
import io.github.udaiyan.showcase.pages.HomePage;
import io.github.udaiyan.showcase.pages.IframesPage;
import io.github.udaiyan.showcase.pages.ModalsPage;
import io.github.udaiyan.showcase.pages.PopupsPage;
import io.github.udaiyan.showcase.pages.TablesPage;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Performance baseline steps.
 *
 * Single-sample wall-clock timing over the public internet is noisy (CDN
 * warmth, server load, ad scripts), so each scenario samples THREE
 * navigations and gates on the MEDIAN. Budgets carry headroom because this
 * is a shared third-party practice site; the gate's job is to catch
 * egregious regressions, not to enforce a precise SLO. Precise percentile
 * gating (p75/p95) belongs in a controlled environment / synthetic monitoring.
 *
 * The quoted {string} page name keeps these steps distinct from the literal
 * "I am on the X page" navigation steps elsewhere (no duplicate-step conflict).
 */
public class PerformanceSteps {

    private static final int SAMPLES = 3;

    private final TestContext context;
    private final FrameworkConfig config;
    private double[] samplesSeconds;
    private double medianSeconds;

    public PerformanceSteps(TestContext context, FrameworkConfig config) {
        this.context = context;
        this.config = config;
    }

    @Given("I am on the {string} page")
    public void iAmOnThePage(String pageName) {
        samplesSeconds = new double[SAMPLES];
        for (int i = 0; i < SAMPLES; i++) {
            long start = System.currentTimeMillis();
            navigate(pageName);
            samplesSeconds[i] = (System.currentTimeMillis() - start) / 1000.0;
        }
        double[] sorted = samplesSeconds.clone();
        Arrays.sort(sorted);
        medianSeconds = sorted[sorted.length / 2];
    }

    private void navigate(String pageName) {
        switch (pageName.toLowerCase()) {
            case "home"        -> new HomePage(context.page()).open(config.baseUrl());
            case "form fields" -> new FormFieldsPage(context.page()).open(config.baseUrl());
            case "popups"      -> new PopupsPage(context.page()).open(config.baseUrl());
            case "modals"      -> new ModalsPage(context.page()).open(config.baseUrl());
            case "iframes"     -> new IframesPage(context.page()).open(config.baseUrl());
            case "tables"      -> new TablesPage(context.page()).open(config.baseUrl());
            default -> throw new IllegalArgumentException("Unknown page: " + pageName);
        }
    }

    @Then("the page should load within {int} seconds")
    public void thePageShouldLoadWithinSeconds(int thresholdSeconds) {
        System.out.println("load samples(s)=" + Arrays.toString(samplesSeconds)
                + " median=" + medianSeconds + " budget=" + thresholdSeconds);
        assertThat(medianSeconds)
                .as("median page load within %ds budget (samples %s)",
                        thresholdSeconds, Arrays.toString(samplesSeconds))
                .isLessThanOrEqualTo(thresholdSeconds);
    }
}
