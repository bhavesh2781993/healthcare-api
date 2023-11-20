CREATE OR REPLACE VIEW public.view_end_of_treatment
AS
SELECT c.clinic_id,
       pt.patient_tracker_id,
       p.patient_id,
       p.mrn,
       p.first_name,
       p.last_name,
       ( SELECT string_agg(treatment_meta_lookup.meta_value::text, ','::text) AS cancer_sites
         FROM treatment_meta_lookup
                  LEFT JOIN patient_treatment_info pti ON pti.patient_tracker_id = pt.patient_tracker_id
         WHERE treatment_meta_lookup.treatment_meta_lookup_id = pti.treatment_site_id
       ) AS cancer_sites,
       pos.diagnosis,
       pos.summary,
       ( SELECT string_agg(pti.treatment_start_date::text, ','::text) AS start_date
         FROM patient_treatment_info pti
         WHERE pti.patient_tracker_id = pt.patient_tracker_id) AS start_date,
       otv.total_dose_delivered,
       otv.total_fractions_delivered,
       wf.name as workflow
FROM patient_trackers pt
         LEFT JOIN patients p ON p.patient_id = pt.patient_id
         LEFT JOIN clinics c ON c.clinic_id = p.clinic_id
         LEFT JOIN patient_oncology_summary pos ON pos.patient_oncology_summary_id = pt.patient_tracker_id
         LEFT JOIN otv_notes otv ON otv.patient_tracker_id = pt.patient_tracker_id
         LEFT JOIN patient_tracker_steps pts ON pts.patient_tracker_id = pt.patient_tracker_id
         LEFT JOIN workflow_steps ws ON ws.workflow_step_id = pts.workflow_step_id
         LEFT JOIN workflows wf on wf.workflow_id = ws.workflow_id
WHERE ws.step::text = 'END_OF_TREATMENT'::text;
