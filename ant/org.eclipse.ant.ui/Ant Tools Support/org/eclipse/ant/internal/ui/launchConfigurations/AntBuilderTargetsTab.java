/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ant.internal.ui.launchConfigurations;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ant.internal.ui.AntUIImages;
import org.eclipse.ant.internal.ui.AntUIPlugin;
import org.eclipse.ant.internal.ui.AntUtil;
import org.eclipse.ant.internal.ui.IAntUIConstants;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.externaltools.internal.model.BuilderUtils;
import org.eclipse.ui.externaltools.internal.model.IExternalToolConstants;
import org.eclipse.ui.externaltools.internal.model.IExternalToolsHelpContextIds;

public class AntBuilderTargetsTab extends AbstractLaunchConfigurationTab {
	
    private ILaunchConfiguration fConfiguration;
    
	private Button fAfterCleanTarget;
	private Button fManualBuildTarget;
	private Button fAutoBuildTarget;
	private Button fDuringCleanTarget;
    
    private Text fAfterCleanTargetText;
    private Text fManualBuildTargetText;
    private Text fAutoBuildTargetText;
    private Text fDuringCleanTargetText;
    
    private Map fAttributeToTargets= new HashMap();
    
    private static final String NO_TARGETS_SELECTED= AntLaunchConfigurationMessages.getString("AntBuilderTargetsTab.0");  //$NON-NLS-1$
    
	private SelectionListener fSelectionListener= new SelectionAdapter() {
		/* (non-Javadoc)
		 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
		 */
		public void widgetSelected(SelectionEvent e) {
            String attribute= null;
			Object source = e.getSource();
            Text text= null;
			if (source == fAfterCleanTarget) {
                attribute= IAntLaunchConfigurationConstants.ATTR_ANT_AFTER_CLEAN_TARGETS;
                text= fAfterCleanTargetText;
			} else if (source == fManualBuildTarget) {
                attribute= IAntLaunchConfigurationConstants.ATTR_ANT_MANUAL_TARGETS;
                text= fManualBuildTargetText;
			} else if (source == fAutoBuildTarget) {
                attribute= IAntLaunchConfigurationConstants.ATTR_ANT_AUTO_TARGETS;
                text= fAutoBuildTargetText;
			} else if (source == fDuringCleanTarget) {
                attribute= IAntLaunchConfigurationConstants.ATTR_ANT_CLEAN_TARGETS;
                text= fDuringCleanTargetText;
			}
			
            setTargets(attribute, text);
            updateLaunchConfigurationDialog();
		}
	};
	
	public AntBuilderTargetsTab() {
		super();
	}
	
	protected void createTargetsComponent(Composite parent) {
	    createLabel(AntLaunchConfigurationMessages.getString("AntBuilderTargetsTab.1"), parent); //$NON-NLS-1$
	    fAfterCleanTargetText= createText(parent);
		fAfterCleanTarget = createPushButton(parent, AntLaunchConfigurationMessages.getString("AntBuilderTargetsTab.2"), null); //$NON-NLS-1$
		GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_END);
		fAfterCleanTarget.setLayoutData(gd);
		fAfterCleanTarget.addSelectionListener(fSelectionListener);
		
		createLabel(AntLaunchConfigurationMessages.getString("AntBuilderTargetsTab.3"), parent); //$NON-NLS-1$
        fManualBuildTargetText = createText(parent);
		fManualBuildTarget = createPushButton(parent, AntLaunchConfigurationMessages.getString("AntBuilderTargetsTab.4"), null); //$NON-NLS-1$
		gd = new GridData(GridData.HORIZONTAL_ALIGN_END);
		fManualBuildTarget.setLayoutData(gd);
		fManualBuildTarget.addSelectionListener(fSelectionListener);
        
        createLabel(AntLaunchConfigurationMessages.getString("AntBuilderTargetsTab.5"), parent); //$NON-NLS-1$
        fAutoBuildTargetText = createText(parent);
		fAutoBuildTarget = createPushButton(parent, AntLaunchConfigurationMessages.getString("AntBuilderTargetsTab.6"), null); //$NON-NLS-1$
		gd = new GridData(GridData.HORIZONTAL_ALIGN_END);
		fAutoBuildTarget.setLayoutData(gd);
		fAutoBuildTarget.addSelectionListener(fSelectionListener);
        
        createLabel(AntLaunchConfigurationMessages.getString("AntBuilderTargetsTab.7"), parent); //$NON-NLS-1$
        fDuringCleanTargetText = createText(parent);
		fDuringCleanTarget = createPushButton(parent, AntLaunchConfigurationMessages.getString("AntBuilderTargetsTab.8"), null); //$NON-NLS-1$
		gd = new GridData(GridData.HORIZONTAL_ALIGN_END);
		fDuringCleanTarget.setLayoutData(gd);
		fDuringCleanTarget.addSelectionListener(fSelectionListener);
	}

    private Label createLabel(String text, Composite parent) {
        Label newLabel= new Label(parent, SWT.NONE);
        newLabel.setText(text);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        newLabel.setLayoutData(gd);
        newLabel.setFont(parent.getFont());
        return newLabel;
    }
    private Text createText(Composite parent) {
        GridData gd;
        Text newText = new Text(parent, SWT.MULTI | SWT.WRAP | SWT.BORDER | SWT.V_SCROLL | SWT.READ_ONLY);
        newText.setFont(parent.getFont());
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.heightHint = 40;
        gd.widthHint = IDialogConstants.ENTRY_FIELD_WIDTH;
        newText.setLayoutData(gd);
        return newText;
    }
    
	protected void setTargets(String attribute, Text text) {
        ILaunchConfigurationWorkingCopy copy= null;
        try {
            copy = fConfiguration.getWorkingCopy();
        } catch (CoreException e) {
           return;
        }
        copy.setAttribute(IAntLaunchConfigurationConstants.ATTR_ANT_TARGETS, (String)fAttributeToTargets.get(attribute));
		SetTargetsDialog dialog= new SetTargetsDialog(getShell(), copy);
        String targetsSelected= null;
		if (dialog.open() == Window.OK) {
		    targetsSelected= dialog.getTargetsSelected();
        }
        if (targetsSelected == null) {
            return; //user cancelled
        }
       
        if (targetsSelected.length() == 0) {
            fAttributeToTargets.remove(attribute);
            text.setText(NO_TARGETS_SELECTED);
            text.setEnabled(false);
         } else {
             text.setEnabled(true);
             fAttributeToTargets.put(attribute, targetsSelected);
             setTargetsForUser(text, targetsSelected, null);
         }
	}

    private void setTargetsForUser(Text text, String targetsSelected, String configTargets) {
        if (targetsSelected == null) {
            if (configTargets == null) {
                text.setText(NO_TARGETS_SELECTED);
                return;
            } 
            targetsSelected= configTargets;
        }
        String[] targets= AntUtil.parseRunTargets(targetsSelected);
        StringBuffer result= new StringBuffer(targets[0]);
        for (int i = 1; i < targets.length; i++) {
             result.append(", "); //$NON-NLS-1$
             result.append(targets[i]);
        }
        text.setText(result.toString());
    }

    public void createControl(Composite parent) {
        Composite mainComposite = new Composite(parent, SWT.NONE);
        setControl(mainComposite);
        PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), IExternalToolsHelpContextIds.EXTERNAL_TOOLS_LAUNCH_CONFIGURATION_DIALOG_BUILDER_TAB);
        
        GridLayout layout = new GridLayout();
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.horizontalSpan = 2;
        layout.numColumns = 2;
        layout.makeColumnsEqualWidth = false;
        layout.horizontalSpacing=0;
        layout.verticalSpacing=0;
        mainComposite.setLayout(layout);
        mainComposite.setLayoutData(gridData);
        mainComposite.setFont(parent.getFont());
        createTargetsComponent(mainComposite);
    }

    public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
        configuration.setAttribute(IExternalToolConstants.ATTR_TRIGGERS_CONFIGURED, true);
    }

    public void initializeFrom(ILaunchConfiguration configuration) {
        fConfiguration= configuration;
        
        fAfterCleanTargetText.setEnabled(false);
        fManualBuildTargetText.setEnabled(false);
        fAutoBuildTargetText.setEnabled(false);
        fDuringCleanTargetText.setEnabled(false);

        initializeBuildKinds(configuration);
        intializeTargets(configuration);
    }

    private void intializeTargets(ILaunchConfiguration configuration) {
        String configTargets= null;
        String autoTargets= null;
        String manualTargets= null;
        String afterCleanTargets= null;
        String duringCleanTargets= null;
        try {
            configTargets= configuration.getAttribute(IAntLaunchConfigurationConstants.ATTR_ANT_TARGETS, (String)null);
            autoTargets= configuration.getAttribute(IAntLaunchConfigurationConstants.ATTR_ANT_AUTO_TARGETS, (String)null);
            manualTargets= configuration.getAttribute(IAntLaunchConfigurationConstants.ATTR_ANT_MANUAL_TARGETS, (String)null);
            afterCleanTargets= configuration.getAttribute(IAntLaunchConfigurationConstants.ATTR_ANT_AFTER_CLEAN_TARGETS, (String)null);
            duringCleanTargets= configuration.getAttribute(IAntLaunchConfigurationConstants.ATTR_ANT_CLEAN_TARGETS, (String)null);
            fAttributeToTargets.put(IAntLaunchConfigurationConstants.ATTR_ANT_AUTO_TARGETS, autoTargets);
            fAttributeToTargets.put(IAntLaunchConfigurationConstants.ATTR_ANT_MANUAL_TARGETS, manualTargets);
            fAttributeToTargets.put(IAntLaunchConfigurationConstants.ATTR_ANT_AFTER_CLEAN_TARGETS, afterCleanTargets);
            fAttributeToTargets.put(IAntLaunchConfigurationConstants.ATTR_ANT_CLEAN_TARGETS, duringCleanTargets);
        } catch (CoreException ce) {
            AntUIPlugin.log("Error reading configuration", ce); //$NON-NLS-1$
        }
        
        setTargetsForUser(fManualBuildTargetText, manualTargets, configTargets);
        setTargetsForUser(fAfterCleanTargetText, afterCleanTargets, configTargets);
        setTargetsForUser(fDuringCleanTargetText, duringCleanTargets, configTargets);
        setTargetsForUser(fAutoBuildTargetText, autoTargets, configTargets);
    }

    private void initializeBuildKinds(ILaunchConfiguration configuration) {
        String buildKindString= null;
        try {
            buildKindString= configuration.getAttribute(IExternalToolConstants.ATTR_RUN_BUILD_KINDS, ""); //$NON-NLS-1$
        } catch (CoreException e) {
            AntUIPlugin.log("Error reading configuration", e); //$NON-NLS-1$
        }
        int buildTypes[]= BuilderUtils.buildTypesToArray(buildKindString);
        for (int i = 0; i < buildTypes.length; i++) {
            switch (buildTypes[i]) {
                case IncrementalProjectBuilder.FULL_BUILD:
                    fAfterCleanTargetText.setEnabled(true);
                    break;
                case IncrementalProjectBuilder.INCREMENTAL_BUILD:
                    fManualBuildTargetText.setEnabled(true);
                    break;
                case IncrementalProjectBuilder.AUTO_BUILD:
                    fAutoBuildTargetText.setEnabled(true);
                    break;
                case IncrementalProjectBuilder.CLEAN_BUILD:
                    fDuringCleanTargetText.setEnabled(true);
                    break;
            }
        }
    }

    public void performApply(ILaunchConfigurationWorkingCopy configuration) {
        StringBuffer buffer= new StringBuffer();
        if (!fAfterCleanTargetText.getText().equals(NO_TARGETS_SELECTED)) {
            buffer.append(IExternalToolConstants.BUILD_TYPE_FULL).append(',');
        } 
        if (!fManualBuildTargetText.getText().equals(NO_TARGETS_SELECTED)){
            buffer.append(IExternalToolConstants.BUILD_TYPE_INCREMENTAL).append(','); 
        } 
        if (!fAutoBuildTargetText.getText().equals(NO_TARGETS_SELECTED)) {
            buffer.append(IExternalToolConstants.BUILD_TYPE_AUTO).append(',');
        }
        if (!fDuringCleanTargetText.getText().equals(NO_TARGETS_SELECTED)) {
            buffer.append(IExternalToolConstants.BUILD_TYPE_CLEAN);
        }
        configuration.setAttribute(IExternalToolConstants.ATTR_RUN_BUILD_KINDS, buffer.toString());
        
        
        String targets= (String) fAttributeToTargets.get(IAntLaunchConfigurationConstants.ATTR_ANT_AFTER_CLEAN_TARGETS);
        configuration.setAttribute(IAntLaunchConfigurationConstants.ATTR_ANT_AFTER_CLEAN_TARGETS, targets);
        targets= (String) fAttributeToTargets.get(IAntLaunchConfigurationConstants.ATTR_ANT_AUTO_TARGETS);
        configuration.setAttribute(IAntLaunchConfigurationConstants.ATTR_ANT_AUTO_TARGETS, targets);
        targets= (String) fAttributeToTargets.get(IAntLaunchConfigurationConstants.ATTR_ANT_MANUAL_TARGETS);
        configuration.setAttribute(IAntLaunchConfigurationConstants.ATTR_ANT_MANUAL_TARGETS, targets);
        targets= (String) fAttributeToTargets.get(IAntLaunchConfigurationConstants.ATTR_ANT_CLEAN_TARGETS);
        configuration.setAttribute(IAntLaunchConfigurationConstants.ATTR_ANT_CLEAN_TARGETS, targets);
    }

    /* (non-Javadoc)
     * @see org.eclipse.debug.ui.ILaunchConfigurationTab#getName()
     */
    public String getName() {
        return AntLaunchConfigurationMessages.getString("AntTargetsTab.Tar&gets_14"); //$NON-NLS-1$
    }
        
    /* (non-Javadoc)
     * @see org.eclipse.debug.ui.ILaunchConfigurationTab#getImage()
     */
    public Image getImage() {
        return AntUIImages.getImage(IAntUIConstants.IMG_TAB_ANT_TARGETS);
    }
}