// Create sample jobs with PERFORMANCE_OPTIMIZED durability for testing

import javaposse.jobdsl.plugin.*
import org.jenkinsci.plugins.workflow.job.WorkflowJob
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition
import org.jenkinsci.plugins.workflow.job.properties.DurabilityHintJobProperty
import org.jenkinsci.plugins.workflow.flow.FlowDurabilityHint

// Configuration
def folderName = "Durability-Test-Folder"
def totalPipelines = 500
def parent = Jenkins.instance

// 1. Create a Folder to keep things clean
def folder = parent.getItem(folderName)
if (folder == null) {
    folder = parent.createProject(com.cloudbees.hudson.plugins.folder.Folder.class, folderName)
}

println "Creating ${totalPipelines} pipelines in '${folderName}'..."

(1..totalPipelines).each { i ->
    def jobName = "Test-Pipeline-${i}"
    def job = folder.getItem(jobName)
    
    if (job == null) {
        job = folder.createProject(WorkflowJob.class, jobName)
    }

    // Assign a simple script
    job.setDefinition(new CpsFlowDefinition("echo 'Testing durability for job ${i}'", true))

    // 2. Set an override on even-numbered jobs to test your audit script
    def hint = FlowDurabilityHint.PERFORMANCE_OPTIMIZED
    def prop = new DurabilityHintJobProperty(hint)
  
    if (i % 2 == 0) {
        // This forces "PERFORMANCE_OPTIMIZED" on 250 of the jobs
        job.addProperty(prop)
    } else {
        // This ensures the other 250 use the "Global Default"
        job.removeProperty(DurabilityHintJobProperty.class)
    }

    if (i % 100 == 0) println "Progress: ${i}/${totalPipelines} created."
}

println "Done! 500 pipelines are ready for testing."
