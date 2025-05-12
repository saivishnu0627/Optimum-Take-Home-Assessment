
-- PL/SQL Procedure to Summarize Total Hours Worked by Each Employee

CREATE OR REPLACE PROCEDURE summarize_total_hours AS
BEGIN
    FOR rec IN (SELECT emp_id, SUM(hours_worked) AS total_hours
                FROM attendance
                GROUP BY emp_id) LOOP
        UPDATE employees
        SET total_hours = rec.total_hours
        WHERE id = rec.emp_id;
        DBMS_OUTPUT.PUT_LINE('Employee ID: ' || rec.emp_id || ', Total Hours: ' || rec.total_hours);
    END LOOP;
END;
/

-- Creating a Scheduler Job to Automate the Weekly Report
BEGIN
    DBMS_SCHEDULER.create_job (
        job_name        => 'WEEKLY_HOURS_SUMMARY_JOB',
        job_type        => 'PLSQL_BLOCK',
        job_action      => 'BEGIN summarize_total_hours; END;',
        start_date      => SYSTIMESTAMP,
        repeat_interval => 'FREQ=WEEKLY; BYDAY=MON',
        enabled         => TRUE
    );
    DBMS_OUTPUT.PUT_LINE('Scheduler job created for weekly hours summary.');
END;
/

-- Verify the Job
SELECT job_name, enabled, next_run_date
FROM dba_scheduler_jobs
WHERE job_name = 'WEEKLY_HOURS_SUMMARY_JOB';

-- End of PL/SQL script
