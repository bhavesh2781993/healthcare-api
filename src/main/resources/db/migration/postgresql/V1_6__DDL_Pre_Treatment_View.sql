CREATE OR REPLACE VIEW public.view_pre_treatment
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
    cu.name AS physician,
    p.first_consult_date AS consult,
    pos.summary AS brief_summary,
    wf.name AS workflow
--     ( SELECT concat(cl.street, cl.city, cl.state) AS clinic_location) AS clinic_location
FROM patient_trackers pt
         LEFT JOIN patients p ON p.patient_id = pt.patient_id
         LEFT JOIN clinics c ON c.clinic_id = p.clinic_id
         LEFT JOIN patient_oncology_summary pos ON pos.patient_oncology_summary_id = pt.patient_tracker_id
         LEFT JOIN clinic_users cu ON cu.clinic_user_id = p.clinic_user_id
         LEFT JOIN patient_tracker_steps pts ON pts.patient_tracker_id = pt.patient_tracker_id
         LEFT JOIN workflow_steps ws ON ws.workflow_step_id = pts.workflow_step_id
         LEFT JOIN workflows wf on wf.workflow_id = ws.workflow_id
WHERE ws.step::text = 'PRE_TREATMENT'::text;
