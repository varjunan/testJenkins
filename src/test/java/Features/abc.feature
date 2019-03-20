@I0840
Feature: validate whether the source data loaded to cdf tables successfully
  Scenario: Validate whether the Customer Data from WCS is loaded into CDF
    Given I have source WCS XMLs in Inbound
    Then the WCS customer data should load into the CDF Master table
