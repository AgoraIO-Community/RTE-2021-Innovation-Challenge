/*  This file is part of the "Simple Waypoint System" project by FLOBUK.
 *  You are only allowed to use these resources if you've bought them from the Unity Asset Store.
 * 	You shall not license, sublicense, sell, resell, transfer, assign, distribute or
 * 	otherwise make available to any third party the Service or the Content. */

using UnityEngine;
using UnityEngine.Events;
using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine.AI;

namespace indoorNav
{
    /// <summary>
    /// Movement script: pathfinding using Unity NavMesh agents.
    /// <summary>
    [RequireComponent(typeof(NavMeshAgent))]
    public class NavMoveit : MonoBehaviour
    {
        /// <summary>
        /// Path component to use for movement.
        /// <summary>
        public NavPathManager pathContainer;

        /// <summary>
        /// Whether this object should start its movement at game launch.
        /// <summary>
        public bool onStart = false;

        /// <summary>
        /// Whether this object should walk to the first waypoint or spawn there.
        /// <summary>
        public bool moveToPath = false;

        /// <summary>
        /// reverse the movement direction on the path, typically used for "pingPong" behavior.
        /// <summary>
        public bool reverse = false;

        /// <summary>
        /// Waypoint index where this object should start its path. 
        /// <summary>
        public int startPoint = 0;

        /// <summary>
        /// Current waypoint indicator on the path. 
        /// <summary>
        [HideInInspector]
        public int currentPoint = 0;

        /// <summary>
        /// Option for closing the path on the "loop" looptype.
        /// <summary>
        public bool closeLoop = false;

        /// <summary>
        /// Whether rotation should be overridden by the NavMesh agent.
        /// <summary>
        public bool updateRotation = true;

        /// <summary>
        /// Supported movement looptypes when moving on the path. 
        /// <summary>
        public enum LoopType
        {
            none,
            loop,
            pingPong,
            random
        }
        public LoopType loopType = LoopType.none;

        /// <summary>
        /// Waypoint array references of the requested path.
        /// <summary>
        [HideInInspector]
        public Transform[] waypoints;

        /// <summary>
        /// UnityEvent invoked on each new movement iteration.
        /// Note that this includes a call when moveToPath starts.
        /// </summary>
        public UnityEvent movementStart;
        public event Action movementStartEvent;

        /// <summary>
        /// UnityEvent invoked per waypoint, delivering the current waypoint index.
        /// Note that on loop types, this could mean double invokes for the same waypoint.
        /// E.g. on ping-pong loop type you can check the reverse flag for more control. 
        /// </summary>
        public WaypointEvent movementChange;
        public event Action<int> movementChangeEvent;

        /// <summary>
        /// UnityEvent invoked on each ending movement iteration.
        /// Note that this is not called when moveToPath ends.
        /// </summary>
        public UnityEvent movementEnd;
        public event Action movementEndEvent;

        //temporary bool to indicate when moveToPath is in progress
        private bool moveToPathBool;
        //reference to the agent component
        private NavMeshAgent agent;
        //looptype random generator
        private System.Random rand = new System.Random();
        //looptype random current waypoint index
        private int rndIndex = 0;
        //coroutine when a wait routine is active
        private Coroutine waitRoutine;
        //bool to indicate whether the move coroutines are active
        private bool isMoving = false;


        //initialize components
        void Awake()
        {
            agent = GetComponent<NavMeshAgent>();
            if (agent && agent.stoppingDistance == 0)
                agent.stoppingDistance = 0.5f;
        }


        //check for automatic initialization
        void Start()
        {
            if (onStart)
                StartMove();
        }


        /// <summary>
        /// Starts movement. Can be called from other scripts to allow start delay.
        /// <summary>
        public void StartMove()
        {
            //don't continue without path container
            if (pathContainer == null)
            {
                Debug.LogWarning(gameObject.name + " has no path! Please set Path Container.");
                return;
            }

            //get Transform array with waypoint positions
            waypoints = new Transform[pathContainer.waypoints.Length];
            Array.Copy(pathContainer.waypoints, waypoints, pathContainer.waypoints.Length);
            //cache bool whether to move to the path
            moveToPathBool = moveToPath;

            //initialize waypoint positions
            startPoint = Mathf.Clamp(startPoint, 0, waypoints.Length - 1);
            currentPoint = startPoint;

            Stop();
            StartCoroutine(Move());
        }


        //constructs the tween and starts movement
        private IEnumerator Move()
        {
            //enable agent updates
            agent.isStopped = false;
            agent.updateRotation = updateRotation;
            isMoving = true;

            //if move to path is enabled,
            //set an additional destination to the first waypoint
            if (moveToPath)
            {
                //moveToPath start event
                movementStart.Invoke();
                if (movementStartEvent != null)
                    movementStartEvent();

                agent.SetDestination(waypoints[currentPoint].position);
                yield return StartCoroutine(WaitForDestination());
                moveToPathBool = false;
            }

            //we're now at the first waypoint position, so directly call the next waypoint.
            //on looptype random we have to initialize a random order of waypoints first.
            if (loopType == LoopType.random)
            {
                RandomizeWaypoints();
                StartCoroutine(NextWaypoint());
                yield break;
            }

            //path start event
            movementStart.Invoke();
            if (movementStartEvent != null)
                movementStartEvent();

            if (moveToPath)
                StartCoroutine(NextWaypoint());
            else
                GoToWaypoint(startPoint);
        }


        //this method moves us one by one to the next waypoint
        //and executes all delay or tweening interaction
        private IEnumerator NextWaypoint()
        {
            //execute events for this waypoint
            OnWaypointChange(currentPoint);
            yield return new WaitForEndOfFrame();

            //check for pausing and wait until unpaused again
            while (waitRoutine != null) yield return null;
            Transform next = null;

            if (loopType == LoopType.random)
            {
                //parse currentPoint value from waypoint
                rndIndex++;
                currentPoint = int.Parse(waypoints[rndIndex].name.Replace("Waypoint ", ""));
                next = waypoints[rndIndex];
            }
            else if(reverse)
            {
                //repeating mode is on: moving backwards
                currentPoint--;
            }
            else
                //default mode: move forwards
                currentPoint++;

            //just to make sure we don't run into an out of bounds exception
            currentPoint = Mathf.Clamp(currentPoint, 0, waypoints.Length - 1);
            //set the next waypoint based on calculated current point
            if (next == null) next = waypoints[currentPoint];

            //set destination to the next waypoint
            agent.SetDestination(next.position);
            yield return StartCoroutine(WaitForDestination());

            //determine if the agent reached the path's end
            if (loopType != LoopType.random && currentPoint == waypoints.Length - 1
                || rndIndex == waypoints.Length - 1 || reverse && currentPoint == 0)
            {
                OnWaypointChange(currentPoint);
                StartCoroutine(ReachedEnd());
            }
            else
                StartCoroutine(NextWaypoint());
        }


        //wait until the agent reached its destination
        private IEnumerator WaitForDestination()
        {
            yield return new WaitForEndOfFrame();         
            while (agent.pathPending)
                yield return null;
            yield return new WaitForEndOfFrame();

            float remain = agent.remainingDistance;
            while (remain == Mathf.Infinity || remain - agent.stoppingDistance > float.Epsilon
            || agent.pathStatus != NavMeshPathStatus.PathComplete)
            {
                remain = agent.remainingDistance;
                yield return null;
            }
        }


        //called at every waypoint to invoke events
        private void OnWaypointChange(int index)
        {
            movementChange.Invoke(index);
            if (movementChangeEvent != null)
                movementChangeEvent(index);
        }


        //object reached the end of its path
        private IEnumerator ReachedEnd()
        {
            movementEnd.Invoke();
            if (movementEndEvent != null)
                movementEndEvent();

            //each looptype has specific properties
            switch (loopType)
            {
                //LoopType.none means there will be no repeat,
                //so we just execute the final event
                case LoopType.none:
                    isMoving = false;
                    yield break;

                //in a loop we set our position indicator back to zero,
                //also executing last event
                case LoopType.loop:
                    int nextPoint = reverse ? waypoints.Length - 1 : 0;

                    //additional option: if the path was closed, we move our object
                    //from the last to the first waypoint instead of just "appearing" there
                    if (closeLoop)
                    {
                        moveToPathBool = true;
                        agent.SetDestination(waypoints[nextPoint].position);
                        yield return StartCoroutine(WaitForDestination());
                        moveToPathBool = false;
                    }
                    else
                        agent.Warp(waypoints[nextPoint].position);

                    currentPoint = nextPoint;

                    //the Move() method and start event is not called after the first iteration anymore
                    //invoke it manually to stay consistent with the splineMove behavior
                    movementStart.Invoke();
                    if (movementStartEvent != null)
                        movementStartEvent();
                    break;

                //on LoopType.pingPong, we have to invert currentPoint updates
                case LoopType.pingPong:
                    reverse = !reverse;

                    //the Move() method and start event is not called after the first iteration anymore
                    //invoke it manually to stay consistent with the splineMove behavior
                    if(!reverse)
                    {
                        movementStart.Invoke();
                        if (movementStartEvent != null)
                            movementStartEvent();
                    }
                    break;

                //on LoopType.random, we calculate a random order between all waypoints
                //and loop through them, for this case we use the Fisher-Yates algorithm
                case LoopType.random:
                    RandomizeWaypoints();
                    break;
            }

            //start moving to the next iteration
            StartCoroutine(NextWaypoint());
        }


        private void RandomizeWaypoints()
        {
            //reinitialize original waypoint positions
            Array.Copy(pathContainer.waypoints, waypoints, pathContainer.waypoints.Length);
            int n = waypoints.Length;

            //shuffle waypoints array
            while (n > 1)
            {
                int k = rand.Next(n--);
                Transform temp = waypoints[n];
                waypoints[n] = waypoints[k];
                waypoints[k] = temp;
            }

            //since all waypoints are shuffled the first waypoint does not
            //correspond with the actual current position, so we have to
            //swap the first waypoint with the actual waypoint
            Transform first = pathContainer.waypoints[currentPoint];
            for (int i = 0; i < waypoints.Length; i++)
            {
                if (waypoints[i] == first)
                {
                    Transform temp = waypoints[0];
                    waypoints[0] = waypoints[i];
                    waypoints[i] = temp;
                    break;
                }
            }

            //reset random loop index
            rndIndex = 0;
        }


        /// <summary>
        /// Teleports to the defined waypoint index on the path.
        /// </summary>
        public void GoToWaypoint(int index)
        {
            Stop();
            isMoving = true;
            currentPoint = index;
            agent.Warp(waypoints[index].position);
            StartCoroutine(NextWaypoint());
        }


        /// <summary>
        /// Pauses the current movement routine for a defined amount of time.
        /// <summary>
        public void Pause(float seconds = 0f)
        {
            if (waitRoutine != null) StopCoroutine(waitRoutine);
            agent.isStopped = true;

            if (seconds > 0)
                waitRoutine = StartCoroutine(Wait(seconds));
        }


        //waiting routine
        private IEnumerator Wait(float secs = 0f)
        {
            yield return new WaitForSeconds(secs);
            Resume();
        }


        /// <summary>
        /// Resumes the current movement routine.
        /// <summary>
        public void Resume()
        {
            if (waitRoutine != null)
            {
                StopCoroutine(waitRoutine);
                waitRoutine = null;
            }

            agent.isStopped = false;
        }
        
        
        /// <summary>
        /// Reverses movement at any time.
        /// <summary>
        public void Reverse()
        {
            //inverse direction toggle
            reverse = !reverse;
            
            //invert starting point from current waypoint
            if(reverse) startPoint = currentPoint - 1;
            else startPoint = currentPoint + 1;
            
            //start new iteration
            moveToPathBool = true;
            StartMove();
        }


        /// <summary>
        /// Changes the current path of this walker object and starts movement.
        /// <summary>
        public void SetPath(NavPathManager newPath)
        {
            //disable any running movement methods
            Stop();
            //set new path container
            pathContainer = newPath;
            //restart movement
            StartMove();
        }


        /// <summary>
        /// Disables any running movement routines.
        /// <summary>
        public void Stop()
        {
            StopAllCoroutines();
            waitRoutine = null;
            isMoving = false;

            if (agent.enabled)
            {
                agent.isStopped = true;
            }
        }


        /// <summary>
        /// Stops movement and resets to the first waypoint.
        /// <summary>
        public void ResetToStart()
        {
            Stop();
            currentPoint = 0;
            if (pathContainer)
            {
                agent.Warp(pathContainer.waypoints[currentPoint].position);
            }
        }


        /// <summary>
        /// Wrapper to change agent speed.
        /// <summary>
        public void ChangeSpeed(float value)
        {
            agent.speed = value;
        }


        /// <summary>
        /// Returns whether the movement coroutines are active.
        /// Still true when moveToPath or paused.
        /// </summary>
        public bool IsMoving()
        {
            return isMoving;
        }


        /// <summary>
        /// Returns whether the movement tween is active and moving to path.
        /// Returns false afterwards.
        /// </summary>
        public bool IsMovingToPath()
        {
            return IsMoving() && moveToPathBool;
        }


        /// <summary>
        /// Returns true if the movement is currently paused.
        /// </summary>
        public bool IsPaused()
        {
            return waitRoutine != null;
        }
    }
}