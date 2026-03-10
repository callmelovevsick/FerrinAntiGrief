# FERRINANTIGRIEF V2.0 - CORE FEATURES

## 1. Advanced Command Obfuscation
* Dynamic Aliasing: Generates unique, randomized aliases for sensitive administrative commands.
* Alias Mutation Mode: Automatically rotates all command aliases every 12 hours or immediately upon detecting a brute-force attempt.
* Sync Webhook: Real-time synchronization of new alias mappings to a secure administrative Discord channel.

## 2. Emergency Lockdown System
* Global Freeze: Instantly restricts movement for all non-administrative players during a security breach.
* Entity Suppression: Blocks the placement of TNT, TNT Minecarts, and the summoning of destructive entities (Wither, EnderDragon).
* Command Blacklist: Hard-blocks high-risk operations including /execute, /give, /fill, and /data during active lockdown.

## 3. Stability and Lag Mitigation
* Redstone Logic Guard: Monitors BlockRedstoneEvent frequency to detect and freeze rapid-pulse lag machines.
* Item Spike Detection: Identifies abnormal item entity counts in localized areas to prevent and log duplication exploits.
* Entity Lag Detector: Executes asynchronous chunk scans every 60 seconds to purge excess non-player entities if TPS drops.

## 4. Behavioral Risk Analysis
* Honeytrap System: Deploys "fake" administrative commands to identify unauthorized users attempting to gain elevated permissions.
* Risk Scoring: Assigns a risk integer to players based on suspicious activity. Automated actions trigger once score thresholds are met.
* Audit Logging: Records every high-risk event into a persistent, searchable administrative log.

## 5. Server Health Monitoring
* Status Diagnostics: Real-time tracking of Ticks Per Second (TPS), memory allocation, loaded chunks, and total entity count.
* Asynchronous Webhooks: Offloads alert processing to secondary threads to ensure zero impact on main-thread performance.



---

## ADMINISTRATIVE PERMISSIONS

| Permission | Description |
| :--- | :--- |
| ferrin.admin.lockdown | Access to toggle emergency lockdown and bypass movement restrictions. |
| ferrin.admin.reload | Permission to refresh plugin configurations and alias mappings. |
| ferrin.status | Permission to view real-time server health and security metrics. |
| ferrin.bypass.risk | Exempts the player from the automated Risk Scoring System. |

---

## TECHNICAL SPECIFICATIONS
* Core Model: Java 17+ / Paper API
* Data Handling: ConcurrentHashMap for thread-safe runtime risk tracking.
* Optimization: Asynchronous Task Scheduler for all I/O and scanning operations.
* Security: SecureRandom implementation for high-entropy alias generation.