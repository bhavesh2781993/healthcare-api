CREATE OR REPLACE VIEW public.view_treatment_planning
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
       ( SELECT json_agg(json_build_object('name', clinic_users.name)) AS treatment_teams
         FROM clinic_users
                  LEFT JOIN treatment_teams tt ON tt.patient_tracker_id = pt.patient_tracker_id
         WHERE clinic_users.clinic_user_id = tt.clinic_user_id) AS treatment_teams,
       ( SELECT string_agg(fusions_lookup.fusions::text, ','::text) AS fusion
         FROM fusions_lookup
                  LEFT JOIN sim_fusions sf ON sf.sim_order_id = so.sim_order_id) AS fusion,
       wf.name AS workflow
FROM patient_trackers pt
         LEFT JOIN patients p ON p.patient_id = pt.patient_id
         LEFT JOIN clinics c ON c.clinic_id = p.clinic_id
         LEFT JOIN patient_oncology_summary pos ON pos.patient_oncology_summary_id = pt.patient_tracker_id
         LEFT JOIN sim_orders so ON so.sim_order_id = pt.patient_tracker_id
         LEFT JOIN patient_tracker_steps pts ON pts.patient_tracker_id = pt.patient_tracker_id
         LEFT JOIN workflow_steps ws ON ws.workflow_step_id = pts.workflow_step_id
         LEFT JOIN workflows wf on wf.workflow_id = ws.workflow_id
WHERE ws.step::text = 'TREATMENT_PLANNING'::text;
