/*******************************************************************************
 * Copyright (c) 2014 Salesforce.com, inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Salesforce.com, inc. - initial API and implementation
 ******************************************************************************/
package com.salesforce.ide.ui.wizards.components.apex.trigger;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import com.salesforce.ide.core.internal.utils.Utils;
import com.salesforce.ide.core.model.ApexTrigger;
import com.salesforce.ide.ui.wizards.components.IComponentWizardPage;
import com.salesforce.ide.ui.wizards.components.apex.ApexCodeWizardComposite;

public class ApexTriggerWizardComposite extends ApexCodeWizardComposite {

    private Group grpTriggerOperations = null;
    private List<Button> checkboxOperationButtons = new ArrayList<Button>();
    private ApexTrigger component = null;
    private List<String> triggerOperationsOptions = null;

    //   C O N S T R U C T O R S
    public ApexTriggerWizardComposite(Composite parent, int style, String componentTypeDisplayName,
            Set<String> supportedApiVesions) {
        super(parent, style, componentTypeDisplayName);
        initialize(supportedApiVesions);
    }

    public ApexTriggerWizardComposite(Composite parent, int style, String componentTypeDisplayName,
            Set<String> supportedApiVesions, List<String> triggerOperationsOptions) {
        super(parent, style, componentTypeDisplayName);
        this.triggerOperationsOptions = triggerOperationsOptions;
        initialize(supportedApiVesions);
    }

    //   M E T H O D S
    public List<String> getTriggerOperationOptions() {
        return triggerOperationsOptions;
    }

    public void setTriggerOperationOptions(List<String> triggerOptions) {
        this.triggerOperationsOptions = triggerOptions;
    }

    public ApexTrigger getComponent() {
        return component;
    }

    public void setComponent(ApexTrigger component) {
        this.component = component;
    }

    protected void initialize(Set<String> supportedApiVesions) {
        setLayout(new GridLayout());

        Group grpProperties = createPropertiesGroup(this);
        createNameText(grpProperties);
        createApiVersionCombo(grpProperties, supportedApiVesions);
        createDetailsGroup(grpProperties);
        createOperationGroup();
        initSize();
    }

    private void createDetailsGroup(Group grpProperties) {
        createObjectTextAndRefreshButton(null, grpProperties);
    }

    private void createOperationGroup() {
        grpTriggerOperations = new Group(this, SWT.NONE);
        grpTriggerOperations.setText("Apex Trigger Operations");
        grpTriggerOperations.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        grpTriggerOperations.setLayout(new GridLayout(4, false));
        checkboxOperationButtons = new ArrayList<Button>();
        // dynamically create trigger operation checkboxes
        if (Utils.isNotEmpty(triggerOperationsOptions)) {
            for (String operation : triggerOperationsOptions) {
                createTriggerOperationButton(operation);
            }
        }
    }

    private Button createTriggerOperationButton(String text) {
        SelectionListener selectionListener = new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                getComponentWizardPage().validateUserInput();
            }

            public void widgetSelected(SelectionEvent e) {
                widgetDefaultSelected(e);
            }
        };

        Button checkboxOperaionButton = new Button(grpTriggerOperations, SWT.CHECK);
        checkboxOperaionButton.setText(text);
        checkboxOperaionButton.addSelectionListener(selectionListener);
        checkboxOperationButtons.add(checkboxOperaionButton);
        return checkboxOperaionButton;
    }

    public void disableTriggerNameField() {
        grpTriggerOperations.setEnabled(false);
    }

    @Override
    public void disableControls() {
        super.disableControls();
        disableTriggerNameField();
    }

    @Override
    public void setComponentWizardPage(IComponentWizardPage componentWizardPage) {
        this.componentWizardPage = componentWizardPage;
    }

    public List<Button> getCheckboxOperationButtons() {
        return checkboxOperationButtons;
    }

    public List<String> getSelectedTriggerOperations() {
        List<String> selectedTriggerOperations = new ArrayList<String>();
        for (Button checkboxOperationButton : checkboxOperationButtons) {
            if (checkboxOperationButton.getSelection()) {
                selectedTriggerOperations.add(checkboxOperationButton.getText());
            }
        }
        return selectedTriggerOperations;
    }

    public void setCheckboxOperationButtons(List<Button> checkboxOperationButtons) {
        this.checkboxOperationButtons = checkboxOperationButtons;
    }
}
