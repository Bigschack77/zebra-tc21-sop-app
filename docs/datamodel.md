# Datamodel – Zebra TC21 SOP App

## Entiteter

### SOP
- id
- title
- createdAt
- updatedAt

### Step
- id
- sopId
- stepNumber
- title
- description
- createdAt
- updatedAt

### StepImage
- id
- stepId
- filePath
- createdAt

## Relationer
- En SOP har mange trin
- Et trin har mange billeder
- Et billede tilhører ét trin
