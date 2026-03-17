// Checks all pipelines to make sure that each is using Maximum Survivablity

import org.jenkinsci.plugins.workflow.job.WorkflowJob
import org.jenkinsci.plugins.workflow.job.properties.DurabilityHintJobProperty

println "Searching for jobs with custom Durability Settings..."
println "-" * 80

def customJobs = 0
def allPipelines = Jenkins.instance.getAllItems(WorkflowJob.class)

allPipelines.each { job ->
    def prop = job.getProperty(DurabilityHintJobProperty.class)
    
    if (prop != null && prop.getHint() != null) {
        customJobs++
        println "JOB: ${job.getFullName()}"
        println "  --> Custom Setting: ${prop.getHint()}"
        println ""
    }
}

println "-" * 80
println "Total Pipeline jobs scanned: ${allPipelines.size()}"
println "Jobs with custom overrides found: ${customJobs}"

if (customJobs == 0) {
    println "Success: All jobs are currently following the Global Default setting."
}
